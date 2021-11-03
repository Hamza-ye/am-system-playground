package org.nmcpye.activitiesmanagement.extended.organisationunit.comparator;

import java.util.Comparator;
import org.nmcpye.activitiesmanagement.domain.organisationunit.OrganisationUnit;

public class OrganisationUnitDisplayNameComparator implements Comparator<OrganisationUnit> {

    public static final Comparator<OrganisationUnit> INSTANCE = new OrganisationUnitDisplayNameComparator();

    @Override
    public int compare(OrganisationUnit organisationUnit1, OrganisationUnit organisationUnit2) {
        return organisationUnit1.getDisplayName().compareTo(organisationUnit2.getDisplayName());
    }
}
