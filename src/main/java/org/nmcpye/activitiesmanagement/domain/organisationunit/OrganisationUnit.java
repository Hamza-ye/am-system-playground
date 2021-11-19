package org.nmcpye.activitiesmanagement.domain.organisationunit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.vividsolutions.jts.geom.Geometry;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.StringUtils;
import org.geotools.geojson.geom.GeometryJSON;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.nmcpye.activitiesmanagement.domain.*;
import org.nmcpye.activitiesmanagement.domain.chv.CHV;
import org.nmcpye.activitiesmanagement.domain.dataset.DataSet;
import org.nmcpye.activitiesmanagement.domain.dataset.DengueCasesReport;
import org.nmcpye.activitiesmanagement.domain.dataset.MalariaCasesReport;
import org.nmcpye.activitiesmanagement.domain.demographicdata.DemographicData;
import org.nmcpye.activitiesmanagement.domain.enumeration.OrganisationUnitType;
import org.nmcpye.activitiesmanagement.domain.person.Person;
import org.nmcpye.activitiesmanagement.extended.common.*;
import org.nmcpye.activitiesmanagement.extended.common.coordinate.CoordinateObject;
import org.nmcpye.activitiesmanagement.extended.common.coordinate.CoordinateUtils;
import org.nmcpye.activitiesmanagement.extended.organisationunit.comparator.OrganisationUnitDisplayNameComparator;
import org.nmcpye.activitiesmanagement.extended.organisationunit.comparator.OrganisationUnitDisplayShortNameComparator;
import org.nmcpye.activitiesmanagement.extended.schema.PropertyType;
import org.nmcpye.activitiesmanagement.extended.schema.annotation.Gist;
import org.nmcpye.activitiesmanagement.extended.schema.annotation.Gist.Include;
import org.nmcpye.activitiesmanagement.extended.schema.annotation.Property;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * A OrganisationUnit.
 */
@Entity
@Table(name = "organisation_unit")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@JsonRootName( value = "organisationUnit", namespace = DxfNamespaces.DXF_2_0 )
public class OrganisationUnit extends BaseDimensionalItemObject implements MetadataObject, CoordinateObject {

    private static final String PATH_SEP = "/";

    public static final String KEY_USER_ORGUNIT = "USER_ORGUNIT";

    public static final String KEY_USER_ORGUNIT_CHILDREN = "USER_ORGUNIT_CHILDREN";

    public static final String KEY_USER_ORGUNIT_GRANDCHILDREN = "USER_ORGUNIT_GRANDCHILDREN";

    public static final String KEY_LEVEL = "LEVEL-";

    public static final String KEY_ORGUNIT_GROUP = "OU_GROUP-";

    private static final String NAME_SEPARATOR = " / ";

    //    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
//    @SequenceGenerator(name = "sequenceGenerator")
//    private Long id;
//
//    @NotNull
//    @Size(max = 11)
//    @Column(name = "uid", length = 11, nullable = false, unique = true)
//    private String uid;
//
//    @Column(name = "code", unique = true)
//    private String code;
//
//    @Column(name = "name")
//    private String name;
//
    @Size(max = 50)
    @Column(name = "short_name", length = 50)
    private String shortName;
//
//    @Column(name = "created")
//    private Date created;
//
//    @Column(name = "last_updated")
//    private Date lastUpdated;

    @Column(name = "path")
    private String path;

    @Column(name = "hierarchy_level")
    private Integer hierarchyLevel;

    @Column(name = "opening_date")
    @Temporal(TemporalType.DATE)
    private Date openingDate;

    @Column(name = "comment")
    private String comment;

    @Column(name = "closed_date")
    @Temporal(TemporalType.DATE)
    private Date closedDate;

    @Column(name = "url")
    private String url;

    @Column(name = "contact_person")
    private String contactPerson;

    @Column(name = "address")
    private String address;

    @Column(name = "email")
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "organisation_unit_type", nullable = false)
    private OrganisationUnitType organisationUnitType;

    @OneToMany(mappedBy = "organisationUnit")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = {"user", "createdBy", "lastUpdatedBy", "reportClass", "period", "dataSet", "organisationUnit"}, allowSetters = true)
    private Set<MalariaCasesReport> malariaReports = new HashSet<>();

    @OneToMany(mappedBy = "organisationUnit")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = {"user", "createdBy", "lastUpdatedBy", "reportClass", "period", "dataSet", "organisationUnit"}, allowSetters = true)
    private Set<DengueCasesReport> dengueReports = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(
        value = {
            "malariaReports",
            "dengueReports",
            "parent",
            "hfHomeSubVillage",
            "coveredByHf",
            "createdBy",
            "user",
            "lastUpdatedBy",
            "malariaUnit",
            "assignedChv",
            "children",
            "demographicData",
            "groups",
            "people",
            "dataViewPeople",
            "dataSets",
        },
        allowSetters = true
    )
    private OrganisationUnit parent;

    /**
     * When OrgUnit is HealthFacility, what its home subvillage
     */
    @ApiModelProperty(value = "When OrgUnit is HealthFacility, what its home subvillage")
    @ManyToOne
    @JsonIgnoreProperties(
        value = {
            "malariaReports",
            "dengueReports",
            "parent",
            "hfHomeSubVillage",
            "coveredByHf",
            "createdBy",
            "user",
            "lastUpdatedBy",
            "malariaUnit",
            "assignedChv",
            "children",
            "demographicData",
            "groups",
            "people",
            "dataViewPeople",
            "dataSets",
        },
        allowSetters = true
    )
    private OrganisationUnit hfHomeSubVillage;

    /**
     * When OrgUnit is HealthFacility, what villages it covers
     */
    @ApiModelProperty(value = "When OrgUnit is HealthFacility, what villages it covers")
    @ManyToOne
    @JsonIgnoreProperties(
        value = {
            "malariaReports",
            "dengueReports",
            "parent",
            "hfHomeSubVillage",
            "coveredByHf",
            "createdBy",
            "user",
            "lastUpdatedBy",
            "malariaUnit",
            "assignedChv",
            "children",
            "demographicData",
            "groups",
            "people",
            "dataViewPeople",
            "dataSets",
        },
        allowSetters = true
    )
    private OrganisationUnit coveredByHf;

    @ManyToOne
    @JsonIgnoreProperties(value = {"organisationUnits", "malariaUnitStaffMembers", "user", "createdBy", "lastUpdatedBy"}, allowSetters = true)
    private MalariaUnit malariaUnit;

    @ManyToOne
    @JsonIgnoreProperties(
        value = {
            "person", "coveredSubVillages", "district", "homeSubvillage", "managedByHf", "user", "createdBy", "lastUpdatedBy", "supervisionTeams",
        },
        allowSetters = true
    )
    private CHV assignedChv;

    @OneToMany(mappedBy = "parent")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = {
            "malariaReports",
            "dengueReports",
            "parent",
            "hfHomeSubVillage",
            "coveredByHf",
            "createdBy",
            "user",
            "lastUpdatedBy",
            "malariaUnit",
            "assignedChv",
            "children",
            "demographicData",
            "groups",
            "people",
            "dataViewPeople",
            "dataSets",
        },
        allowSetters = true
    )
    private Set<OrganisationUnit> children = new HashSet<>();

    @OneToMany(mappedBy = "organisationUnit")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = {"organisationUnit", "user", "createdBy", "lastUpdatedBy", "source"}, allowSetters = true)
    private Set<DemographicData> demographicData = new HashSet<>();

    @ManyToMany(mappedBy = "members")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = {"user", "createdBy", "lastUpdatedBy", "members", "groupSets"}, allowSetters = true)
    private Set<OrganisationUnitGroup> groups = new HashSet<>();

    @ManyToMany(mappedBy = "organisationUnits")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = {
            "userInfo", "user", "createdBy", "lastUpdatedBy", "organisationUnits", "dataViewOrganisationUnits", "personAuthorityGroups", "groups",
        },
        allowSetters = true
    )
    private Set<Person> people = new HashSet<>();

//    @ManyToMany(mappedBy = "dataViewOrganisationUnits")
//    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
//    @JsonIgnoreProperties(
//        value = {
//            "userInfo", "user", "createdBy", "lastUpdatedBy", "organisationUnits", "dataViewOrganisationUnits", "personAuthorityGroups", "groups",
//        },
//        allowSetters = true
//    )
//    private Set<Person> dataViewPeople = new HashSet<>();

    @ManyToMany(mappedBy = "sources")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = {"malariaCasesReports", "dengueCasesReports", "periodType", "notificationRecipients", "user", "createdBy", "lastUpdatedBy", "sources"},
        allowSetters = true
    )
    private Set<DataSet> dataSets = new HashSet<>();

    @Column(columnDefinition = "geometry(Geometry,4326)", nullable = true)
    private Geometry geometry;

    private transient boolean currentParent;

    private transient String type;

    private transient List<String> groupNames = new ArrayList<>();

    private transient Double value;

    private transient Integer memberCount;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public OrganisationUnit() {
        setAutoFields(); // Must be set to get UID and have getPath work properly
    }

    public OrganisationUnit(String name) {
        this();
        this.name = name;
    }

    /**
     * @param name        OrgUnit name
     * @param shortName   OrgUnit short name
     * @param code        OrgUnit code
     * @param openingDate OrgUnit opening date
     * @param closedDate  OrgUnit closing date
     * @param comment     a comment
     */
    public OrganisationUnit(String name, String shortName, String code, Date openingDate, Date closedDate, String comment) {
        this(name);
        this.shortName = shortName;
        this.code = code;
        this.openingDate = openingDate;
        this.closedDate = closedDate;
        this.comment = comment;
    }

    /**
     * @param name        OrgUnit name
     * @param parent      parent {@link OrganisationUnit}
     * @param shortName   OrgUnit short name
     * @param code        OrgUnit code
     * @param openingDate OrgUnit opening date
     * @param closedDate  OrgUnit closing date
     * @param comment     a comment
     */
    public OrganisationUnit(
        String name,
        OrganisationUnit parent,
        String shortName,
        String code,
        Date openingDate,
        Date closedDate,
        String comment,
        OrganisationUnitType organisationUnitType) {
        this(name);
        this.parent = parent;
        this.shortName = shortName;
        this.code = code;
        this.organisationUnitType = organisationUnitType;
        this.openingDate = openingDate;
        this.closedDate = closedDate;
        this.comment = comment;
    }

    @Override
    public void setAutoFields() {
        super.setAutoFields();
    }

    // jhipster-needle-entity-add-field - JHipster will add fields here
//    @Override
//    public Long getId() {
//        return id;
//    }
//
//    @Override
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public OrganisationUnit id(Long id) {
//        this.id = id;
//        return this;
//    }
//
//    @Override
//    public String getUid() {
//        return this.uid;
//    }
//
//    public OrganisationUnit uid(String uid) {
//        this.uid = uid;
//        return this;
//    }

//    @Override
//    public void setUid(String uid) {
//        this.uid = uid;
//    }
//
//    @Override
//    public String getCode() {
//        return this.code;
//    }

    public OrganisationUnit code(String code) {
        this.code = code;
        return this;
    }

//    @Override
//    public void setCode(String code) {
//        this.code = code;
//    }
//
//    @Override
//    public String getName() {
//        return this.name;
//    }

    public OrganisationUnit name(String name) {
        this.name = name;
        return this;
    }

    //    @Override
//    public void setName(String name) {
//        this.name = name;
//    }
//
    @Override
    public String getShortName() {
        return this.shortName;
    }

    public OrganisationUnit shortName(String shortName) {
        this.shortName = shortName;
        return this;
    }

//    @Override
//    public void setShortName(String shortName) {
//        this.shortName = shortName;
//    }
//
//    @Override
//    public Date getCreated() {
//        return this.created;
//    }

    public OrganisationUnit created(Date created) {
        this.created = created;
        return this;
    }

//    @Override
//    public void setCreated(Date created) {
//        this.created = created;
//    }
//
//    @Override
//    public Date getLastUpdated() {
//        return this.lastUpdated;
//    }

    public OrganisationUnit lastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
        return this;
    }

//    @Override
//    public void setLastUpdated(Date lastUpdated) {
//        this.lastUpdated = lastUpdated;
//    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setCoveredByHf(OrganisationUnit organisationUnit) {
        this.coveredByHf = organisationUnit;
    }

//    public User getUser() {
//        return this.user;
//    }

    public OrganisationUnit user(User user) {
        this.setUser(user);
        return this;
    }

//    public void setUser(User user) {
//        this.user = user;
//    }
//
//    public User getLastUpdatedBy() {
//        return this.lastUpdatedBy;
//    }

    public OrganisationUnit lastUpdatedBy(User user) {
        this.setLastUpdatedBy(user);
        return this;
    }

//    public void setLastUpdatedBy(User user) {
//        this.lastUpdatedBy = user;
//    }

    public OrganisationUnit addChildren(OrganisationUnit organisationUnit) {
        this.children.add(organisationUnit);
        organisationUnit.setParent(this);
        return this;
    }

    public OrganisationUnit removeChildren(OrganisationUnit organisationUnit) {
        this.children.remove(organisationUnit);
        organisationUnit.setParent(null);
        return this;
    }

    public OrganisationUnit addGroup(OrganisationUnitGroup organisationUnitGroup) {
        this.groups.add(organisationUnitGroup);
        organisationUnitGroup.getMembers().add(this);
        return this;
    }

    public OrganisationUnit removeGroup(OrganisationUnitGroup organisationUnitGroup) {
        this.groups.remove(organisationUnitGroup);
        organisationUnitGroup.getMembers().remove(this);
        return this;
    }

    public OrganisationUnit addPerson(Person person) {
        this.people.add(person);
        person.getOrganisationUnits().add(this);
        return this;
    }

    public OrganisationUnit removePerson(Person person) {
        this.people.remove(person);
        person.getOrganisationUnits().remove(this);
        return this;
    }

    public OrganisationUnit dataSets(Set<DataSet> dataSets) {
        this.setDataSets(dataSets);
        return this;
    }

    public void addOrganisationUnitGroup(OrganisationUnitGroup organisationUnitGroup) {
        groups.add(organisationUnitGroup);
        organisationUnitGroup.getMembers().add(this);
    }

    public void removeOrganisationUnitGroup(OrganisationUnitGroup organisationUnitGroup) {
        groups.remove(organisationUnitGroup);
        organisationUnitGroup.getMembers().remove(this);
    }

    public void removeAllOrganisationUnitGroups() {
        for (OrganisationUnitGroup organisationUnitGroup : groups) {
            organisationUnitGroup.getMembers().remove(this);
        }

        groups.clear();
    }

    public void removeAllUsers() {
        for (Person user : people) {
            user.getOrganisationUnits().remove(this);
        }

        people.clear();
    }

    public List<OrganisationUnit> getSortedChildren(SortProperty sortBy) {
        List<OrganisationUnit> sortedChildren = new ArrayList<>(children);

        Comparator<OrganisationUnit> comparator = SortProperty.SHORT_NAME == sortBy
            ? OrganisationUnitDisplayShortNameComparator.INSTANCE
            : OrganisationUnitDisplayNameComparator.INSTANCE;

        Collections.sort(sortedChildren, comparator);
        return sortedChildren;
    }

    public List<OrganisationUnit> getSortedChildren() {
        return getSortedChildren(SortProperty.NAME);
    }

    public static List<OrganisationUnit> getSortedChildren(Collection<OrganisationUnit> units) {
        return getSortedChildren(units, SortProperty.NAME);
    }

    public static List<OrganisationUnit> getSortedChildren(Collection<OrganisationUnit> units, SortProperty sortBy) {
        List<OrganisationUnit> children = new ArrayList<>();

        for (OrganisationUnit unit : units) {
            children.addAll(unit.getSortedChildren(sortBy));
        }

        return children;
    }

    public static List<OrganisationUnit> getSortedGrandChildren(Collection<OrganisationUnit> units) {
        return getSortedGrandChildren(units, SortProperty.NAME);
    }

    public static List<OrganisationUnit> getSortedGrandChildren(Collection<OrganisationUnit> units, SortProperty sortBy) {
        List<OrganisationUnit> children = new ArrayList<>();

        for (OrganisationUnit unit : units) {
            children.addAll(unit.getSortedGrandChildren(sortBy));
        }

        return children;
    }

    public Set<OrganisationUnit> getGrandChildren() {
        Set<OrganisationUnit> grandChildren = new HashSet<>();

        for (OrganisationUnit child : children) {
            grandChildren.addAll(child.getChildren());
        }

        return grandChildren;
    }

    public List<OrganisationUnit> getSortedGrandChildren() {
        return getSortedGrandChildren(SortProperty.NAME);
    }

    public List<OrganisationUnit> getSortedGrandChildren(SortProperty sortBy) {
        List<OrganisationUnit> grandChildren = new ArrayList<>();

        for (OrganisationUnit child : getSortedChildren(sortBy)) {
            grandChildren.addAll(child.getSortedChildren(sortBy));
        }

        return grandChildren;
    }

    public boolean hasChild() {
        return !this.children.isEmpty();
    }

    //    @JsonProperty
    public boolean isLeaf() {
        return children == null || children.isEmpty();
    }

    @Override
    public boolean hasDescendantsWithCoordinates() {
        return CoordinateUtils.hasDescendantsWithCoordinates(children);
    }

    public boolean isDescendant(OrganisationUnit ancestor) {
        if (ancestor == null) {
            return false;
        }

        OrganisationUnit unit = this;

        while (unit != null) {
            if (ancestor.equals(unit)) {
                return true;
            }

            unit = unit.getParent();
        }

        return false;
    }

    public boolean isDescendant(Set<OrganisationUnit> ancestors) {
        if (ancestors == null || ancestors.isEmpty()) {
            return false;
        }
        Set<String> ancestorsUid = ancestors.stream().map(OrganisationUnit::getUid).collect(Collectors.toSet());

        OrganisationUnit unit = this;

        while (unit != null) {
            if (ancestorsUid.contains(unit.getUid())) {
                return true;
            }

            unit = unit.getParent();
        }

        return false;
    }

    public boolean hasCoordinatesUp() {
        if (parent != null) {
            if (parent.getParent() != null) {
                return parent.getParent().hasDescendantsWithCoordinates();
            }
        }

        return false;
    }

    public OrganisationUnitGroup getGroupInGroupSet(OrganisationUnitGroupSet groupSet) {
        if (groupSet != null) {
            for (OrganisationUnitGroup group : groups) {
                if (groupSet.getOrganisationUnitGroups().contains(group)) {
                    return group;
                }
            }
        }

        return null;
    }

    public Long getGroupIdInGroupSet(OrganisationUnitGroupSet groupSet) {
        final OrganisationUnitGroup group = getGroupInGroupSet(groupSet);

        return group != null ? group.getId() : null;
    }

    public String getGroupNameInGroupSet(OrganisationUnitGroupSet groupSet) {
        final OrganisationUnitGroup group = getGroupInGroupSet(groupSet);

        return group != null ? group.getName() : null;
    }

    public String getAncestorNames() {
        List<OrganisationUnit> units = getAncestors();

        StringBuilder builder = new StringBuilder();

        for (OrganisationUnit unit : units) {
            builder.append(unit.getName()).append(NAME_SEPARATOR);
        }

        return builder.toString();
    }

    /**
     * Returns the list of ancestor organisation units for this organisation unit.
     * Does not include itself. The list is ordered by root first.
     *
     * @throws IllegalStateException if circular parent relationships is detected.
     */
    @JsonProperty("ancestors")
    @JsonSerialize(contentAs = BaseIdentifiableObject.class)
    public List<OrganisationUnit> getAncestors() {
        List<OrganisationUnit> units = new ArrayList<>();
        Set<OrganisationUnit> visitedUnits = new HashSet<>();

        OrganisationUnit unit = parent;

        while (unit != null) {
            if (!visitedUnits.add(unit)) {
                throw new IllegalStateException(
                    "Organisation unit '" + this.toString() + "' has circular parent relationships: '" + unit + "'"
                );
            }

            units.add(unit);
            unit = unit.getParent();
        }

        Collections.reverse(units);
        return units;
    }

    /**
     * Returns the list of ancestor organisation units up to any of the given roots
     * for this organisation unit. Does not include itself. The list is ordered by
     * root first.
     *
     * @param roots the root organisation units, if null using real roots.
     */
    public List<OrganisationUnit> getAncestors(Collection<OrganisationUnit> roots) {
        List<OrganisationUnit> units = new ArrayList<>();
        OrganisationUnit unit = parent;

        while (unit != null) {
            units.add(unit);

            if (roots != null && roots.contains(unit)) {
                break;
            }

            unit = unit.getParent();
        }

        Collections.reverse(units);
        return units;
    }

    /**
     * Returns the list of ancestor organisation unit names up to any of the given
     * roots for this organisation unit. The list is ordered by root first.
     *
     * @param roots the root organisation units, if null using real roots.
     */
    public List<String> getAncestorNames(Collection<OrganisationUnit> roots, boolean includeThis) {
        List<String> units = new ArrayList<>();

        if (includeThis) {
            units.add(getDisplayName());
        }

        OrganisationUnit unit = parent;

        while (unit != null) {
            units.add(unit.getDisplayName());

            if (roots != null && roots.contains(unit)) {
                break;
            }

            unit = unit.getParent();
        }

        Collections.reverse(units);
        return units;
    }

    /**
     * Returns the list of ancestor organisation unit UIDs up to any of the given
     * roots for this organisation unit. Does not include itself. The list is
     * ordered by root first.
     *
     * @param rootUids the root organisation units, if null using real roots.
     */
    public List<String> getAncestorUids(Set<String> rootUids) {
        if (path == null || path.isEmpty()) {
            return Lists.newArrayList();
        }

        String[] ancestors = path.substring(1).split(PATH_SEP); // Skip first delimiter, root unit first
        int lastIndex = ancestors.length - 2; // Skip this unit
        List<String> uids = Lists.newArrayList();

        for (int i = lastIndex; i >= 0; i--) {
            String uid = ancestors[i];
            uids.add(0, uid);

            if (rootUids != null && rootUids.contains(uid)) {
                break;
            }
        }

        return uids;
    }

    public void updateParent(OrganisationUnit newParent) {
        if (this.parent != null && this.parent.getChildren() != null) {
            this.parent.getChildren().remove(this);
        }

        this.parent = newParent;

        newParent.getChildren().add(this);
    }

    public Set<OrganisationUnit> getChildrenThisIfEmpty() {
        Set<OrganisationUnit> set = new HashSet<>();

        if (hasChild()) {
            set = children;
        } else {
            set.add(this);
        }

        return set;
    }

    @JsonProperty(value = "level", access = JsonProperty.Access.READ_ONLY)
    public int getLevel() {
        return StringUtils.countMatches(path, PATH_SEP);
    }

    protected void setLevel(int level) {
        // ignored, just used by persistence framework
    }

    /**
     * Returns a string representing the graph of ancestors. The string is delimited
     * by "/". The ancestors are ordered by root first and represented by UIDs.
     *
     * @param roots the root organisation units, if null using real roots.
     */
    public String getParentGraph(Collection<OrganisationUnit> roots) {
        Set<String> rootUids = roots != null ? Sets.newHashSet(IdentifiableObjectUtils.getUids(roots)) : null;
        List<String> ancestors = getAncestorUids(rootUids);
        return StringUtils.join(ancestors, PATH_SEP);
    }

    /**
     * Returns a string representing the graph of ancestors. The string is delimited
     * by "/". The ancestors are ordered by root first and represented by names.
     *
     * @param roots       the root organisation units, if null using real roots.
     * @param includeThis whether to include this organisation unit in the graph.
     */
    public String getParentNameGraph(Collection<OrganisationUnit> roots, boolean includeThis) {
        StringBuilder builder = new StringBuilder();

        List<OrganisationUnit> ancestors = getAncestors(roots);

        for (OrganisationUnit unit : ancestors) {
            builder.append("/").append(unit.getName());
        }

        if (includeThis) {
            builder.append("/").append(name);
        }

        return builder.toString();
    }

    /**
     * Returns a mapping between the uid and the uid parent graph of the given
     * organisation units.
     */
    public static Map<String, String> getParentGraphMap(List<OrganisationUnit> organisationUnits, Collection<OrganisationUnit> roots) {
        Map<String, String> map = new HashMap<>();

        if (organisationUnits != null) {
            for (OrganisationUnit unit : organisationUnits) {
                map.put(unit.getUid(), unit.getParentGraph(roots));
            }
        }

        return map;
    }

    /**
     * Returns a mapping between the uid and the name parent graph of the given
     * organisation units.
     */
    public static Map<String, String> getParentNameGraphMap(
        List<OrganisationUnit> organisationUnits,
        Collection<OrganisationUnit> roots,
        boolean includeThis
    ) {
        Map<String, String> map = new HashMap<>();

        if (organisationUnits != null) {
            for (OrganisationUnit unit : organisationUnits) {
                map.put(unit.getUid(), unit.getParentNameGraph(roots, includeThis));
            }
        }

        return map;
    }

    // -------------------------------------------------------------------------
    // Getters and setters
    // -------------------------------------------------------------------------

    @JsonProperty
    @JsonSerialize(as = BaseIdentifiableObject.class)
    public OrganisationUnit getParent() {
        return parent;
    }

    public void setParent(OrganisationUnit parent) {
        this.parent = parent;
    }

    @JsonProperty
    public String getPath() {
        List<String> pathList = new ArrayList<>();
        Set<String> visitedSet = new HashSet<>();
        OrganisationUnit currentParent = parent;

        pathList.add(uid);

        while (currentParent != null) {
            if (!visitedSet.contains(currentParent.getUid())) {
                pathList.add(currentParent.getUid());
                visitedSet.add(currentParent.getUid());
                currentParent = currentParent.getParent();
            } else {
                currentParent = null; // Protect against cyclic org unit graphs
            }
        }

        Collections.reverse(pathList);

        path = PATH_SEP + StringUtils.join(pathList, PATH_SEP);

        return path;
    }

    /**
     * Do not set directly, managed by persistence layer.
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * Used by persistence layer. Purpose is to have a column for use in database
     * queries. For application use see {@link OrganisationUnit#getLevel()} which
     * has better performance.
     */
    public Integer getHierarchyLevel() {
        Set<String> uids = Sets.newHashSet(uid);

        OrganisationUnit current = this;

        while ((current = current.getParent()) != null) {
            boolean add = uids.add(current.getUid());

            if (!add) {
                break; // Protect against cyclic org unit graphs
            }
        }

        hierarchyLevel = uids.size();

        return hierarchyLevel;
    }

    /**
     * Do not set directly.
     */
    public void setHierarchyLevel(Integer hierarchyLevel) {
        this.hierarchyLevel = hierarchyLevel;
    }

    @JsonProperty
    @JsonSerialize(contentUsing = JacksonOrganisationUnitChildrenSerializer.class)
    public Set<OrganisationUnit> getChildren() {
        return children;
    }

    public void setChildren(Set<OrganisationUnit> children) {
        this.children = children;
    }

    @JsonProperty
    public Date getOpeningDate() {
        return openingDate;
    }

    public void setOpeningDate(Date openingDate) {
        this.openingDate = openingDate;
    }

    @JsonProperty
    public Date getClosedDate() {
        return closedDate;
    }

    public void setClosedDate(Date closedDate) {
        this.closedDate = closedDate;
    }

    @JsonProperty
    public String getComment() {
        return comment;
    }

    @JsonProperty
    @Property(PropertyType.URL)
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @JsonProperty
    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    @JsonProperty
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @JsonProperty
    //    @Property( PropertyType.EMAIL )
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @JsonProperty
    @Property(PropertyType.PHONENUMBER)
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @JsonProperty
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("organisationUnitGroups")
    @JsonSerialize(contentAs = BaseIdentifiableObject.class)
    public Set<OrganisationUnitGroup> getGroups() {
        return groups;
    }

    public void setGroups(Set<OrganisationUnitGroup> groups) {
        this.groups = groups;
    }

    @JsonProperty
    @JsonSerialize(contentAs = BaseIdentifiableObject.class)
    public Set<Person> getPeople() {
        return people;
    }

    public void setPeople(Set<Person> people) {
        this.people = people;
    }

    @Gist(included = Include.FALSE)
    @JsonProperty
    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    @Override
    public FeatureType getFeatureType() {
        return geometry != null ? FeatureType.getTypeFromName(this.geometry.getGeometryType()) : null;
    }

    @Override
    public String getCoordinates() {
        if (geometry != null) {
            return extractCoordinates(this.getGeometry());
        }
        return "";
    }

    @Override
    public boolean hasCoordinates() {
        return this.geometry != null;
    }

    // -------------------------------------------------------------------------
    // DimensionalItemObject
    // -------------------------------------------------------------------------

    @Override
    public DimensionItemType getDimensionItemType() {
        return DimensionItemType.ORGANISATION_UNIT;
    }

    // -------------------------------------------------------------------------
    // Getters and setters for transient fields
    // -------------------------------------------------------------------------

    public List<String> getGroupNames() {
        return groupNames;
    }

    public void setGroupNames(List<String> groupNames) {
        this.groupNames = groupNames;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public boolean isCurrentParent() {
        return currentParent;
    }

    public void setCurrentParent(boolean currentParent) {
        this.currentParent = currentParent;
    }

    @JsonProperty
    public Integer getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(Integer memberCount) {
        this.memberCount = memberCount;
    }

    public String getGeometryAsJson() {
        GeometryJSON geometryJSON = new GeometryJSON();

        return this.geometry != null ? geometryJSON.toString(this.geometry) : null;
    }

    /**
     * Set the Geometry field using a GeoJSON
     * (https://en.wikipedia.org/wiki/GeoJSON) String, like {"type":"Point",
     * "coordinates":[....]}
     *
     * @param geometryAsJsonString String containing a GeoJSON JSON payload
     * @throws IOException an error occurs parsing the payload
     */
    public void setGeometryAsJson(String geometryAsJsonString) throws IOException {
        if (!Strings.isNullOrEmpty(geometryAsJsonString)) {
            GeometryJSON geometryJSON = new GeometryJSON();

            Geometry geometry = geometryJSON.read(geometryAsJsonString);

            geometry.setSRID(4326);

            this.geometry = geometry;
        }
    }

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public OrganisationUnit id(Long id) {
        this.id = id;
        return this;
    }

    public OrganisationUnit uid(String uid) {
        this.uid = uid;
        return this;
    }

    public OrganisationUnit path(String path) {
        this.path = path;
        return this;
    }

    public OrganisationUnit hierarchyLevel(Integer hierarchyLevel) {
        this.hierarchyLevel = hierarchyLevel;
        return this;
    }

    public OrganisationUnit openingDate(Date openingDate) {
        this.openingDate = openingDate;
        return this;
    }

    public OrganisationUnit comment(String comment) {
        this.comment = comment;
        return this;
    }

    public OrganisationUnit closedDate(Date closedDate) {
        this.closedDate = closedDate;
        return this;
    }

    public OrganisationUnit url(String url) {
        this.url = url;
        return this;
    }

    public OrganisationUnit contactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
        return this;
    }

    public OrganisationUnit address(String address) {
        this.address = address;
        return this;
    }

    public OrganisationUnit email(String email) {
        this.email = email;
        return this;
    }

    public OrganisationUnit phoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    @JsonProperty
    public OrganisationUnitType getOrganisationUnitType() {
        return this.organisationUnitType;
    }

    public OrganisationUnit organisationUnitType(OrganisationUnitType organisationUnitType) {
        this.organisationUnitType = organisationUnitType;
        return this;
    }

    public void setOrganisationUnitType(OrganisationUnitType organisationUnitType) {
        this.organisationUnitType = organisationUnitType;
    }

    @JsonProperty
    //    @JsonSerialize( contentAs = BaseIdentifiableObject.class )
    public Set<MalariaCasesReport> getMalariaReports() {
        return this.malariaReports;
    }

    public OrganisationUnit malariaReports(Set<MalariaCasesReport> malariaCasesReports) {
        this.setMalariaReports(malariaCasesReports);
        return this;
    }

    public OrganisationUnit addMalariaReport(MalariaCasesReport malariaCasesReport) {
        this.malariaReports.add(malariaCasesReport);
        malariaCasesReport.setOrganisationUnit(this);
        return this;
    }

    public OrganisationUnit removeMalariaReport(MalariaCasesReport malariaCasesReport) {
        this.malariaReports.remove(malariaCasesReport);
        malariaCasesReport.setOrganisationUnit(null);
        return this;
    }

    public void setMalariaReports(Set<MalariaCasesReport> malariaCasesReports) {
        if (this.malariaReports != null) {
            this.malariaReports.forEach(i -> i.setOrganisationUnit(null));
        }
        if (malariaCasesReports != null) {
            malariaCasesReports.forEach(i -> i.setOrganisationUnit(this));
        }
        this.malariaReports = malariaCasesReports;
    }

    @JsonProperty
    //    @JsonSerialize( contentAs = BaseIdentifiableObject.class )
    public Set<DengueCasesReport> getDengueReports() {
        return this.dengueReports;
    }

    public OrganisationUnit dengueReports(Set<DengueCasesReport> dengueCasesReports) {
        this.setDengueReports(dengueCasesReports);
        return this;
    }

    public OrganisationUnit addDengueReport(DengueCasesReport dengueCasesReport) {
        this.dengueReports.add(dengueCasesReport);
        dengueCasesReport.setOrganisationUnit(this);
        return this;
    }

    public OrganisationUnit removeDengueReport(DengueCasesReport dengueCasesReport) {
        this.dengueReports.remove(dengueCasesReport);
        dengueCasesReport.setOrganisationUnit(null);
        return this;
    }

    public void setDengueReports(Set<DengueCasesReport> dengueCasesReports) {
        if (this.dengueReports != null) {
            this.dengueReports.forEach(i -> i.setOrganisationUnit(null));
        }
        if (dengueCasesReports != null) {
            dengueCasesReports.forEach(i -> i.setOrganisationUnit(this));
        }
        this.dengueReports = dengueCasesReports;
    }

    public OrganisationUnit parent(OrganisationUnit organisationUnit) {
        this.setParent(organisationUnit);
        return this;
    }

    @JsonProperty
    @JsonSerialize(contentAs = BaseIdentifiableObject.class)
    public OrganisationUnit getHfHomeSubVillage() {
        return this.hfHomeSubVillage;
    }

    public OrganisationUnit hfHomeSubVillage(OrganisationUnit organisationUnit) {
        this.setHfHomeSubVillage(organisationUnit);
        return this;
    }

    public void setHfHomeSubVillage(OrganisationUnit organisationUnit) {
        this.hfHomeSubVillage = organisationUnit;
    }

    @JsonProperty
    @JsonSerialize(contentAs = BaseIdentifiableObject.class)
    public OrganisationUnit getCoveredByHf() {
        return this.coveredByHf;
    }

    public OrganisationUnit coveredByHf(OrganisationUnit organisationUnit) {
        this.setCoveredByHf(organisationUnit);
        return this;
    }

    @JsonProperty
    //    @JsonSerialize(contentAs = BaseIdentifiableObject.class)
    public MalariaUnit getMalariaUnit() {
        return this.malariaUnit;
    }

    public OrganisationUnit malariaUnit(MalariaUnit malariaUnit) {
        this.setMalariaUnit(malariaUnit);
        return this;
    }

    public void setMalariaUnit(MalariaUnit malariaUnit) {
        this.malariaUnit = malariaUnit;
    }

    @JsonProperty
    //    @JsonSerialize(contentAs = BaseIdentifiableObject.class)
    public CHV getAssignedChv() {
        return this.assignedChv;
    }

    public OrganisationUnit assignedChv(CHV cHV) {
        this.setAssignedChv(cHV);
        return this;
    }

    public void setAssignedChv(CHV cHV) {
        this.assignedChv = cHV;
    }

    @JsonProperty
    //    @JsonSerialize(contentAs = BaseIdentifiableObject.class)
    public Set<DemographicData> getDemographicData() {
        return this.demographicData;
    }

    public OrganisationUnit demographicData(Set<DemographicData> demographicData) {
        this.setDemographicData(demographicData);
        return this;
    }

    public OrganisationUnit addDemographicData(DemographicData demographicData) {
        this.demographicData.add(demographicData);
        demographicData.setOrganisationUnit(this);
        return this;
    }

    public OrganisationUnit removeDemographicData(DemographicData demographicData) {
        this.demographicData.remove(demographicData);
        demographicData.setOrganisationUnit(null);
        return this;
    }

    public void setDemographicData(Set<DemographicData> demographicData) {
        if (this.demographicData != null) {
            this.demographicData.forEach(i -> i.setOrganisationUnit(null));
        }
        if (demographicData != null) {
            demographicData.forEach(i -> i.setOrganisationUnit(this));
        }
        this.demographicData = demographicData;
    }

    public void addDataSet(DataSet dataSet) {
        dataSets.add(dataSet);
        dataSet.getSources().add(this);
    }

    public void removeDataSet(DataSet dataSet) {
        dataSets.remove(dataSet);
        dataSet.getSources().remove(this);
    }

    public void removeAllDataSets() {
        for (DataSet dataSet : dataSets) {
            dataSet.getSources().remove(this);
        }

        dataSets.clear();
    }

    public void updateDataSets(Set<DataSet> updates) {
        Set<DataSet> toRemove = Sets.difference(dataSets, updates);
        Set<DataSet> toAdd = Sets.difference(updates, dataSets);

        toRemove.forEach(d -> d.getSources().remove(this));
        toAdd.forEach(d -> d.getSources().add(this));

        dataSets.clear();
        dataSets.addAll(updates);
    }

    @JsonProperty
    @JsonSerialize(contentAs = BaseIdentifiableObject.class)
    public Set<DataSet> getDataSets() {
        return dataSets;
    }

    public void addUser(Person user) {
        people.add(user);
        user.getOrganisationUnits().add(this);
    }

    public void removeUser(Person user) {
        people.remove(user);
        user.getOrganisationUnits().remove(this);
    }

    public void setDataSets(Set<DataSet> dataSets) {
        if (this.dataSets != null) {
            this.dataSets.forEach(i -> i.removeOrganisationUnit(this));
        }
        if (dataSets != null) {
            dataSets.forEach(i -> i.addOrganisationUnit(this));
        }
        this.dataSets = dataSets;
    }
}
