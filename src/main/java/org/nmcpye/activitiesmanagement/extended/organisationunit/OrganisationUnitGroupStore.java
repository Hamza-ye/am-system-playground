package org.nmcpye.activitiesmanagement.extended.organisationunit;

import java.util.List;
import org.nmcpye.activitiesmanagement.domain.organisationunit.OrganisationUnitGroup;
import org.nmcpye.activitiesmanagement.extended.common.IdentifiableObjectStore;

public interface OrganisationUnitGroupStore extends IdentifiableObjectStore<OrganisationUnitGroup> {
    List<OrganisationUnitGroup> getOrganisationUnitGroupsWithGroupSets();
}
