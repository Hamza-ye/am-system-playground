package org.nmcpye.activitiesmanagement.extended.organisationunit;

import org.nmcpye.activitiesmanagement.domain.organisationunit.OrganisationUnit;
import org.nmcpye.activitiesmanagement.domain.organisationunit.OrganisationUnitGroup;
import org.nmcpye.activitiesmanagement.domain.organisationunit.OrganisationUnitGroupSet;
import org.nmcpye.activitiesmanagement.extended.common.filter.FilterUtils;
import org.nmcpye.activitiesmanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;

@Service("org.nmcpye.activitiesmanagement.extended.organisationunit.OrganisationUnitGroupService")
public class DefaultOrganisationUnitGroupService implements OrganisationUnitGroupService {

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private final OrganisationUnitGroupStore organisationUnitGroupStore;

    private final OrganisationUnitGroupSetStore organisationUnitGroupSetStore;

    public DefaultOrganisationUnitGroupService(
        OrganisationUnitGroupStore organisationUnitGroupStore,
        OrganisationUnitGroupSetStore organisationUnitGroupSetStore
    ) {
        checkNotNull(organisationUnitGroupSetStore);
        checkNotNull(organisationUnitGroupStore);

        this.organisationUnitGroupStore = organisationUnitGroupStore;
        this.organisationUnitGroupSetStore = organisationUnitGroupSetStore;
    }

    @Autowired
    private UserService currentUserService;

    @Autowired
    private OrganisationUnitService organisationUnitService;

    // -------------------------------------------------------------------------
    // OrganisationUnitGroup
    // -------------------------------------------------------------------------

    @Override
    @Transactional
    public Long addOrganisationUnitGroup(OrganisationUnitGroup organisationUnitGroup) {
        organisationUnitGroupStore.save(organisationUnitGroup);

        return organisationUnitGroup.getId();
    }

    @Override
    @Transactional
    public void updateOrganisationUnitGroup(OrganisationUnitGroup organisationUnitGroup) {
        organisationUnitGroupStore.update(organisationUnitGroup);
    }

    @Override
    @Transactional
    public void deleteOrganisationUnitGroup(OrganisationUnitGroup organisationUnitGroup) {
        organisationUnitGroupStore.delete(organisationUnitGroup);
    }

    @Override
    @Transactional(readOnly = true)
    public OrganisationUnitGroup getOrganisationUnitGroup(Long id) {
        return organisationUnitGroupStore.get(id);
    }

    @Override
    @Transactional(readOnly = true)
    public OrganisationUnitGroup getOrganisationUnitGroup(String uid) {
        return organisationUnitGroupStore.getByUid(uid);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrganisationUnitGroup> getAllOrganisationUnitGroups() {
        return organisationUnitGroupStore.getAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrganisationUnitGroup> getOrganisationUnitGroupsWithGroupSets() {
        return organisationUnitGroupStore.getOrganisationUnitGroupsWithGroupSets();
    }

    // -------------------------------------------------------------------------
    // OrganisationUnitGroupSet
    // -------------------------------------------------------------------------

    @Override
    @Transactional
    public Long addOrganisationUnitGroupSet(OrganisationUnitGroupSet organisationUnitGroupSet) {
        organisationUnitGroupSetStore.save(organisationUnitGroupSet);

        return organisationUnitGroupSet.getId();
    }

    @Override
    @Transactional
    public void updateOrganisationUnitGroupSet(OrganisationUnitGroupSet organisationUnitGroupSet) {
        organisationUnitGroupSetStore.update(organisationUnitGroupSet);
    }

    @Override
    @Transactional
    public void deleteOrganisationUnitGroupSet(OrganisationUnitGroupSet organisationUnitGroupSet) {
        organisationUnitGroupSetStore.delete(organisationUnitGroupSet);
    }

    @Override
    @Transactional(readOnly = true)
    public OrganisationUnitGroupSet getOrganisationUnitGroupSet(Long id) {
        return organisationUnitGroupSetStore.get(id);
    }

    @Override
    @Transactional(readOnly = true)
    public OrganisationUnitGroupSet getOrganisationUnitGroupSet(String uid) {
        return organisationUnitGroupSetStore.getByUid(uid);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrganisationUnitGroupSet> getAllOrganisationUnitGroupSets() {
        return organisationUnitGroupSetStore.getAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrganisationUnitGroupSet> getCompulsoryOrganisationUnitGroupSets() {
        List<OrganisationUnitGroupSet> groupSets = new ArrayList<>();

        for (OrganisationUnitGroupSet groupSet : getAllOrganisationUnitGroupSets()) {
            if (groupSet.isCompulsory()) {
                groupSets.add(groupSet);
            }
        }

        return groupSets;
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrganisationUnitGroupSet> getCompulsoryOrganisationUnitGroupSetsWithMembers() {
        return FilterUtils.filter(getAllOrganisationUnitGroupSets(), object -> object.isCompulsory() && object.hasOrganisationUnitGroups());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrganisationUnitGroupSet> getCompulsoryOrganisationUnitGroupSetsNotAssignedTo(OrganisationUnit organisationUnit) {
        List<OrganisationUnitGroupSet> groupSets = new ArrayList<>();

        for (OrganisationUnitGroupSet groupSet : getCompulsoryOrganisationUnitGroupSets()) {
            if (!groupSet.isMemberOfOrganisationUnitGroups(organisationUnit) && groupSet.hasOrganisationUnitGroups()) {
                groupSets.add(groupSet);
            }
        }

        return groupSets;
    }

    @Override
    @Transactional
    public void mergeWithCurrentUserOrganisationUnits(
        OrganisationUnitGroup organisationUnitGroup,
        Collection<OrganisationUnit> mergeOrganisationUnits
    ) {
        Set<OrganisationUnit> organisationUnits = new HashSet<>(organisationUnitGroup.getMembers());

        Set<OrganisationUnit> userOrganisationUnits = new HashSet<>();

        for (OrganisationUnit organisationUnit : currentUserService.getUserWithAuthorities().get().getPerson().getOrganisationUnits()) {
            userOrganisationUnits.addAll(organisationUnitService.getOrganisationUnitWithChildren(organisationUnit.getUid()));
        }

        organisationUnits.removeAll(userOrganisationUnits);
        organisationUnits.addAll(mergeOrganisationUnits);

        organisationUnitGroup.updateOrganisationUnits(organisationUnits);

        updateOrganisationUnitGroup(organisationUnitGroup);
    }
}
