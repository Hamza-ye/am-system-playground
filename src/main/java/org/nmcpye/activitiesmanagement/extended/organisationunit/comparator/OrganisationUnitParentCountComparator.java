package org.nmcpye.activitiesmanagement.extended.organisationunit.comparator;

import org.nmcpye.activitiesmanagement.domain.organisationunit.OrganisationUnit;
import org.nmcpye.activitiesmanagement.extended.common.IdentifiableObject;

import java.util.Comparator;

public class OrganisationUnitParentCountComparator implements Comparator<IdentifiableObject> {

    @Override
    public int compare(IdentifiableObject organisationUnit1, IdentifiableObject organisationUnit2) {
        Integer parents1 = ((OrganisationUnit) organisationUnit1).getAncestors().size();
        Integer parents2 = ((OrganisationUnit) organisationUnit2).getAncestors().size();

        return parents1.compareTo(parents2);
    }
}
