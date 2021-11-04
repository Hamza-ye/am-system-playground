package org.nmcpye.activitiesmanagement.extended.organisationunit;

import org.nmcpye.activitiesmanagement.domain.organisationunit.OrganisationUnitGroup;
import org.nmcpye.activitiesmanagement.extended.common.IdentifiableObjectStore;

import java.util.List;

public interface OrganisationUnitGroupStore extends IdentifiableObjectStore<OrganisationUnitGroup> {
    List<OrganisationUnitGroup> getOrganisationUnitGroupsWithGroupSets();
}
