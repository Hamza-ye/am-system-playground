package org.nmcpye.activitiesmanagement.extended.organisationunit;

import com.google.common.collect.Sets;
import org.apache.commons.lang3.ObjectUtils;
import org.nmcpye.activitiesmanagement.domain.User;
import org.nmcpye.activitiesmanagement.domain.organisationunit.OrganisationUnit;
import org.nmcpye.activitiesmanagement.domain.organisationunit.OrganisationUnitGroup;
import org.nmcpye.activitiesmanagement.domain.organisationunit.OrganisationUnitLevel;
import org.nmcpye.activitiesmanagement.extended.common.DisplayProperty;
import org.nmcpye.activitiesmanagement.extended.common.SortProperty;
import org.nmcpye.activitiesmanagement.extended.common.collection.ListUtils;
import org.nmcpye.activitiesmanagement.extended.common.filter.FilterUtils;
import org.nmcpye.activitiesmanagement.extended.hierarchy.HierarchyViolationException;
import org.nmcpye.activitiesmanagement.extended.organisationunit.comparator.OrganisationUnitLevelComparator;
import org.nmcpye.activitiesmanagement.extended.organisationunit.pagingrepository.OrganisationUnitPagingRepository;
import org.nmcpye.activitiesmanagement.extended.systemmodule.system.filter.OrganisationUnitPolygonCoveringCoordinateFilter;
import org.nmcpye.activitiesmanagement.extended.systemmodule.system.util.GeoUtils;
import org.nmcpye.activitiesmanagement.extended.systemmodule.system.util.ValidationUtils;
import org.nmcpye.activitiesmanagement.service.UserService;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.geom.Point2D;
import java.util.*;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

@Service("org.nmcpye.activitiesmanagement.extended.organisationunit.OrganisationUnitServiceExt")
public class DefaultOrganisationUnitService implements OrganisationUnitServiceExt {

    private static final String LEVEL_PREFIX = "Level ";
    static final String UID_EXPRESSION = "[a-zA-Z]\\w{10}";
    static final String INT_EXPRESSION = "^(0|-?[1-9]\\d*)$";

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private final Environment env;

    private final OrganisationUnitPagingRepository organisationUnitStore;

    private final OrganisationUnitLevelStore organisationUnitLevelStore;

    private UserService userService;

    public DefaultOrganisationUnitService(
        Environment env,
        OrganisationUnitPagingRepository organisationUnitStore,
        OrganisationUnitLevelStore organisationUnitLevelStore,
        UserService userService
    ) {
        checkNotNull(env);
        checkNotNull(organisationUnitStore);
        checkNotNull(organisationUnitLevelStore);
        checkNotNull(userService);

        this.env = env;
        this.organisationUnitStore = organisationUnitStore;
        this.organisationUnitLevelStore = organisationUnitLevelStore;
        this.userService = userService;
    }

    /**
     * Used only by test harness. Remove after test refactoring
     */
    @Deprecated
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    //    @PostConstruct
    //    public void init()
    //    {
    //        IN_USER_ORG_UNIT_HIERARCHY_CACHE = cacheProvider.newCacheBuilder( Boolean.class )
    //            .forRegion( "inUserOuHierarchy" ).expireAfterWrite( 3, TimeUnit.HOURS ).withInitialCapacity( 1000 )
    //            .forceInMemory().withMaximumSize( SystemUtils.isTestRun( env.getActiveProfiles() ) ? 0 : 20000 ).build();
    //
    //        IN_USER_ORG_UNIT_SEARCH_HIERARCHY_CACHE = cacheProvider.newCacheBuilder( Boolean.class )
    //            .forRegion( "inUserSearchOuHierarchy" ).expireAfterWrite( 3, TimeUnit.HOURS ).withInitialCapacity( 1000 )
    //            .forceInMemory().withMaximumSize( SystemUtils.isTestRun( env.getActiveProfiles() ) ? 0 : 20000 ).build();
    //    }

    // -------------------------------------------------------------------------
    // OrganisationUnit
    // -------------------------------------------------------------------------

    @Override
    @Transactional
    public Long addOrganisationUnit(OrganisationUnit organisationUnit) {
        organisationUnitStore.saveObject(organisationUnit);
        User user = userService.getUserWithAuthorities().orElse(null);

        if (organisationUnit.getParent() == null && user != null && user.getPerson() != null) {
            // Adding a new root node, add this node to the current user
            user.getPerson().getOrganisationUnits().add(organisationUnit);
        }

        return organisationUnit.getId();
    }

    @Override
    @Transactional
    public void updateOrganisationUnit(OrganisationUnit organisationUnit) {
        organisationUnitStore.update(organisationUnit);
    }

    @Override
    @Transactional
    public void updateOrganisationUnit(OrganisationUnit organisationUnit, boolean updateHierarchy) {
        updateOrganisationUnit(organisationUnit);
    }

    @Override
    @Transactional
    public void deleteOrganisationUnit(OrganisationUnit organisationUnit) throws HierarchyViolationException {
        organisationUnitStore.delete(organisationUnit);
    }

    @Override
    @Transactional(readOnly = true)
    public OrganisationUnit getOrganisationUnit(Long id) {
        return organisationUnitStore.get(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrganisationUnit> getAllOrganisationUnits() {
        return organisationUnitStore.getAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrganisationUnit> getAllOrganisationUnitsByLastUpdated(Date lastUpdated) {
        return organisationUnitStore.getAllOrganisationUnitsByLastUpdated(lastUpdated);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrganisationUnit> getOrganisationUnits(Collection<Long> identifiers) {
        return organisationUnitStore.getById(identifiers);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrganisationUnit> getOrganisationUnitsByUid(Collection<String> uids) {
        return organisationUnitStore.getByUid(uids);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrganisationUnit> getOrganisationUnitsByQuery(OrganisationUnitQueryParams params) {
        return organisationUnitStore.getOrganisationUnits(params);
    }

    @Override
    @Transactional(readOnly = true)
    public OrganisationUnit getOrganisationUnit(String uid) {
        return organisationUnitStore.getByUid(uid);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrganisationUnit> getOrganisationUnitByName(String name) {
        return new ArrayList<>(organisationUnitStore.getAllEqName(name));
    }

    @Override
    @Transactional(readOnly = true)
    public OrganisationUnit getOrganisationUnitByCode(String code) {
        return organisationUnitStore.getByCode(code);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrganisationUnit> getRootOrganisationUnits() {
        return organisationUnitStore.getRootOrganisationUnits();
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrganisationUnit> getOrganisationUnits(Collection<OrganisationUnitGroup> groups, Collection<OrganisationUnit> parents) {
        OrganisationUnitQueryParams params = new OrganisationUnitQueryParams();
        params.setParents(Sets.newHashSet(parents));
        params.setGroups(Sets.newHashSet(groups));

        return organisationUnitStore.getOrganisationUnits(params);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrganisationUnit> getOrganisationUnitsWithChildren(Collection<String> parentUids) {
        return getOrganisationUnitsWithChildren(parentUids, null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrganisationUnit> getOrganisationUnitsWithChildren(Collection<String> parentUids, Integer maxLevels) {
        List<OrganisationUnit> units = new ArrayList<>();

        for (String uid : parentUids) {
            units.addAll(getOrganisationUnitWithChildren(uid, maxLevels));
        }

        return units;
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrganisationUnit> getOrganisationUnitWithChildren(String uid) {
        return getOrganisationUnitWithChildren(uid, null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrganisationUnit> getOrganisationUnitWithChildren(String uid, Integer maxLevels) {
        OrganisationUnit unit = getOrganisationUnit(uid);

        Long id = unit != null ? unit.getId() : -1;

        return getOrganisationUnitWithChildren(id, maxLevels);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrganisationUnit> getOrganisationUnitWithChildren(Long id) {
        return getOrganisationUnitWithChildren(id, null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrganisationUnit> getOrganisationUnitWithChildren(Long id, Integer maxLevels) {
        OrganisationUnit organisationUnit = getOrganisationUnit(id);

        if (organisationUnit == null) {
            return new ArrayList<>();
        }

        if (maxLevels != null && maxLevels <= 0) {
            return new ArrayList<>();
        }

        int rootLevel = organisationUnit.getLevel();

        Integer levels = maxLevels != null ? (rootLevel + maxLevels - 1) : null;
                SortProperty orderBy = SortProperty
                   .fromValue(DisplayProperty.class.toString());

        OrganisationUnitQueryParams params = new OrganisationUnitQueryParams();
        params.setParents(Sets.newHashSet(organisationUnit));
        params.setMaxLevels(levels);
        params.setFetchChildren(true);
        params.setOrderBy( orderBy );

        return organisationUnitStore.getOrganisationUnits(params);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrganisationUnit> getOrganisationUnitsAtLevel(int level) {
        OrganisationUnitQueryParams params = new OrganisationUnitQueryParams();
        params.setLevels(Sets.newHashSet(level));

        return organisationUnitStore.getOrganisationUnits(params);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrganisationUnit> getOrganisationUnitsAtLevel(int level, OrganisationUnit parent) {
        OrganisationUnitQueryParams params = new OrganisationUnitQueryParams();
        params.setLevels(Sets.newHashSet(level));

        if (parent != null) {
            params.setParents(Sets.newHashSet(parent));
        }

        return organisationUnitStore.getOrganisationUnits(params);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrganisationUnit> getOrganisationUnitsAtOrgUnitLevels(
        Collection<OrganisationUnitLevel> levels,
        Collection<OrganisationUnit> parents) {
        return getOrganisationUnitsAtLevels(
            levels.stream().map(OrganisationUnitLevel::getLevel).collect(Collectors.toList()),
            parents);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrganisationUnit> getOrganisationUnitsAtLevels(Collection<Integer> levels, Collection<OrganisationUnit> parents) {
        OrganisationUnitQueryParams params = new OrganisationUnitQueryParams();
        params.setLevels(Sets.newHashSet(levels));
        params.setParents(Sets.newHashSet(parents));

        return organisationUnitStore.getOrganisationUnits(params);
    }

    @Override
    @Transactional(readOnly = true)
    public int getNumberOfOrganisationalLevels() {
        return organisationUnitStore.getMaxLevel();
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrganisationUnit> getOrganisationUnitsWithoutGroups() {
        return organisationUnitStore.getOrganisationUnitsWithoutGroups();
    }

    @Override
    @Transactional(readOnly = true)
    public Long getOrganisationUnitHierarchyMemberCount(OrganisationUnit parent, Object member, String collectionName) {
        return organisationUnitStore.getOrganisationUnitHierarchyMemberCount(parent, member, collectionName);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrganisationUnit> getOrganisationUnitsBetweenByName(String name, int first, int max) {
        return organisationUnitStore.getAllLikeName(name, first, max);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isInUserHierarchy(OrganisationUnit organisationUnit) {
        return isInUserHierarchy(userService.getUserWithAuthorities().orElse(null), organisationUnit);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isInUserHierarchy(User user, OrganisationUnit organisationUnit) {
        if (
            user == null ||
                user.getPerson() == null ||
                user.getPerson().getOrganisationUnits() == null ||
                user.getPerson().getOrganisationUnits().isEmpty()
        ) {
            return false;
        }

        return organisationUnit.isDescendant(user.getPerson().getOrganisationUnits());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isInUserHierarchy(String uid, Set<OrganisationUnit> organisationUnits) {
        OrganisationUnit organisationUnit = organisationUnitStore.getByUid(uid);

        return organisationUnit != null && organisationUnit.isDescendant(organisationUnits);
    }

    // -------------------------------------------------------------------------
    // OrganisationUnitHierarchy
    // -------------------------------------------------------------------------

    @Override
    @Transactional(readOnly = true)
    public OrganisationUnitHierarchy getOrganisationUnitHierarchy() {
        return organisationUnitStore.getOrganisationUnitHierarchy();
    }

    @Override
    @Transactional
    public void updateOrganisationUnitParent(Long organisationUnitId, Long parentId) {
        organisationUnitStore.updateOrganisationUnitParent(organisationUnitId, parentId);
    }

    // -------------------------------------------------------------------------
    // OrganisationUnitLevel
    // -------------------------------------------------------------------------

    @Override
    @Transactional
    public Long addOrganisationUnitLevel(OrganisationUnitLevel organisationUnitLevel) {
        organisationUnitLevelStore.saveObject(organisationUnitLevel);
        return organisationUnitLevel.getId();
    }

    @Override
    @Transactional
    public void updateOrganisationUnitLevel(OrganisationUnitLevel organisationUnitLevel) {
        organisationUnitLevelStore.update(organisationUnitLevel);
    }

    @Override
    @Transactional
    public void addOrUpdateOrganisationUnitLevel(OrganisationUnitLevel level) {
        OrganisationUnitLevel existing = getOrganisationUnitLevelByLevel(level.getLevel());

        if (existing == null) {
            addOrganisationUnitLevel(level);
        } else {
            existing.setName(level.getName());
            existing.setOfflineLevels(level.getOfflineLevels());

            updateOrganisationUnitLevel(existing);
        }
    }

    @Override
    @Transactional
    public void pruneOrganisationUnitLevels(Set<Integer> currentLevels) {
        for (OrganisationUnitLevel level : getOrganisationUnitLevels()) {
            if (!currentLevels.contains(level.getLevel())) {
                deleteOrganisationUnitLevel(level);
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public OrganisationUnitLevel getOrganisationUnitLevel(Long id) {
        return organisationUnitLevelStore.get(id);
    }

    @Override
    @Transactional(readOnly = true)
    public OrganisationUnitLevel getOrganisationUnitLevel(String uid) {
        return organisationUnitLevelStore.getByUid(uid);
    }

    @Override
    @Transactional
    public void deleteOrganisationUnitLevel(OrganisationUnitLevel organisationUnitLevel) {
        organisationUnitLevelStore.delete(organisationUnitLevel);
    }

    @Override
    @Transactional
    public void deleteOrganisationUnitLevels() {
        organisationUnitLevelStore.deleteAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrganisationUnitLevel> getOrganisationUnitLevels() {
        return ListUtils.sort(organisationUnitLevelStore.getAll(), OrganisationUnitLevelComparator.INSTANCE);
    }

    @Override
    @Transactional(readOnly = true)
    public OrganisationUnitLevel getOrganisationUnitLevelByLevel(int level) {
        return organisationUnitLevelStore.getByLevel(level);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrganisationUnitLevel> getOrganisationUnitLevelByName(String name) {
        return new ArrayList<>(organisationUnitLevelStore.getAllEqName(name));
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrganisationUnitLevel> getFilledOrganisationUnitLevels() {
        Map<Integer, OrganisationUnitLevel> levelMap = getOrganisationUnitLevelMap();

        List<OrganisationUnitLevel> levels = new ArrayList<>();

        int levelNo = getNumberOfOrganisationalLevels();

        for (int i = 0; i < levelNo; i++) {
            int level = i + 1;

            OrganisationUnitLevel filledLevel = ObjectUtils.firstNonNull(
                levelMap.get(level),
                new OrganisationUnitLevel(level, LEVEL_PREFIX + level)
            );

            levels.add(filledLevel);
        }

        return levels;
    }

    @Override
    @Transactional(readOnly = true)
    public Map<Integer, OrganisationUnitLevel> getOrganisationUnitLevelMap() {
        Map<Integer, OrganisationUnitLevel> levelMap = new HashMap<>();

        List<OrganisationUnitLevel> levels = getOrganisationUnitLevels();

        for (OrganisationUnitLevel level : levels) {
            levelMap.put(level.getLevel(), level);
        }

        return levelMap;
    }

    @Override
    @Transactional(readOnly = true)
    public int getNumberOfOrganisationUnits() {
        return organisationUnitStore.getCount();
    }

    @Override
    @Transactional(readOnly = true)
    public int getOfflineOrganisationUnitLevels() {
        // ---------------------------------------------------------------------
        // Get level from organisation unit of current user
        // ---------------------------------------------------------------------

        User user = userService.getUserWithAuthorities().orElse(null);

        if (user != null && user.getPerson() != null && user.getPerson().hasOrganisationUnit()) {
            OrganisationUnit organisationUnit = user.getPerson().getOrganisationUnit();

            int level = organisationUnit.getLevel();

            OrganisationUnitLevel orgUnitLevel = getOrganisationUnitLevelByLevel(level);

            if (orgUnitLevel != null && orgUnitLevel.getOfflineLevels() != null) {
                return orgUnitLevel.getOfflineLevels();
            }
        }

        // ---------------------------------------------------------------------
        // Get level from system configuration
        // ---------------------------------------------------------------------

        //        OrganisationUnitLevel level = configurationService.getConfiguration().getOfflineOrganisationUnitLevel();
        //
        //        if ( level != null )
        //        {
        //            return level.getLevel();
        //        }

        // ---------------------------------------------------------------------
        // Get max level
        // ---------------------------------------------------------------------

        int max = getOrganisationUnitLevels().size();

        OrganisationUnitLevel maxLevel = getOrganisationUnitLevelByLevel(max);

        if (maxLevel != null) {
            return maxLevel.getLevel();
        }

        // ---------------------------------------------------------------------
        // Return 1 level as fall back
        // ---------------------------------------------------------------------

        return 1;
    }

    @Override
    @Transactional
    public void updatePaths() {
        organisationUnitStore.updatePaths();
    }

    @Override
    @Transactional
    public void forceUpdatePaths() {
        organisationUnitStore.forceUpdatePaths();
    }

    @Override
    @Transactional(readOnly = true)
    public Integer getOrganisationUnitLevelByLevelOrUid(String level) {
        if (level.matches(INT_EXPRESSION)) {
            int orgUnitLevel = Integer.parseInt(level);

            return orgUnitLevel > 0 ? orgUnitLevel : null;
        } else if (level.matches(UID_EXPRESSION)) {
            OrganisationUnitLevel orgUnitLevel = getOrganisationUnitLevel(level);

            return orgUnitLevel != null ? orgUnitLevel.getLevel() : null;
        }

        return null;
    }

    /**
     * Get all the Organisation Units within the distance of a coordinate.
     */
    @Override
    @Transactional(readOnly = true)
    public List<OrganisationUnit> getOrganisationUnitWithinDistance(double longitude, double latitude, double distance) {
        List<OrganisationUnit> objects = organisationUnitStore.getWithinCoordinateArea(GeoUtils.getBoxShape(longitude, latitude, distance));

        // Go through the list and remove the ones located outside radius

        if (objects != null && objects.size() > 0) {
            Iterator<OrganisationUnit> iter = objects.iterator();

            Point2D centerPoint = new Point2D.Double(longitude, latitude);

            while (iter.hasNext()) {
                OrganisationUnit orgunit = iter.next();

                double distancebetween = GeoUtils.getDistanceBetweenTwoPoints(
                    centerPoint,
                    ValidationUtils.getCoordinatePoint2D(GeoUtils.getCoordinatesFromGeometry(orgunit.getGeometry()))
                );

                if (distancebetween > distance) {
                    iter.remove();
                }
            }
        }

        return objects;
    }

    /**
     * Get lowest level/target level Organisation Units that includes the
     * coordinates.
     */
    @Override
    @Transactional(readOnly = true)
    public List<OrganisationUnit> getOrganisationUnitByCoordinate(
        double longitude,
        double latitude,
        String topOrgUnitUid,
        Integer targetLevel
    ) {
        List<OrganisationUnit> orgUnits = new ArrayList<>();

        if (GeoUtils.checkGeoJsonPointValid(longitude, latitude)) {
            OrganisationUnit topOrgUnit = null;

            if (topOrgUnitUid != null && !topOrgUnitUid.isEmpty()) {
                topOrgUnit = getOrganisationUnit(topOrgUnitUid);
            } else {
                // Get top search point through top level org unit which contains coordinate

                List<OrganisationUnit> orgUnitsTopLevel = getTopLevelOrgUnitWithPoint(
                    longitude,
                    latitude,
                    1,
                    getNumberOfOrganisationalLevels() - 1
                );

                if (orgUnitsTopLevel.size() == 1) {
                    topOrgUnit = orgUnitsTopLevel.iterator().next();
                }
            }

            // Search children org units to get the lowest level org unit that contains
            // coordinate

            if (topOrgUnit != null) {
                List<OrganisationUnit> orgUnitChildren;

                if (targetLevel != null) {
                    orgUnitChildren = getOrganisationUnitsAtLevel(targetLevel, topOrgUnit);
                } else {
                    orgUnitChildren = getOrganisationUnitWithChildren(topOrgUnit.getId());
                }

                FilterUtils.filter(orgUnitChildren, new OrganisationUnitPolygonCoveringCoordinateFilter(longitude, latitude));

                // Get org units with lowest level

                int bottomLevel = topOrgUnit.getLevel();

                for (OrganisationUnit ou : orgUnitChildren) {
                    if (ou.getLevel() > bottomLevel) {
                        bottomLevel = ou.getLevel();
                    }
                }

                for (OrganisationUnit ou : orgUnitChildren) {
                    if (ou.getLevel() == bottomLevel) {
                        orgUnits.add(ou);
                    }
                }
            }
        }

        return orgUnits;
    }

    // -------------------------------------------------------------------------
    // Supportive methods
    // -------------------------------------------------------------------------

    /**
     * Searches organisation units until finding one with polygon containing point.
     */
    private List<OrganisationUnit> getTopLevelOrgUnitWithPoint(double longitude, double latitude, int searchLevel, int stopLevel) {
        for (int i = searchLevel; i <= stopLevel; i++) {
            List<OrganisationUnit> unitsAtLevel = new ArrayList<>(getOrganisationUnitsAtLevel(i));
            FilterUtils.filter(unitsAtLevel, new OrganisationUnitPolygonCoveringCoordinateFilter(longitude, latitude));

            if (unitsAtLevel.size() > 0) {
                return unitsAtLevel;
            }
        }

        return new ArrayList<>();
    }
}
