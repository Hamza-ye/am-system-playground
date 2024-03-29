package org.nmcpye.activitiesmanagement.extended.organisationunit.comparator;

import org.nmcpye.activitiesmanagement.domain.organisationunit.OrganisationUnit;

import java.util.Comparator;

public class OrganisationUnitByLevelComparator implements Comparator<OrganisationUnit> {

    public static final Comparator<OrganisationUnit> INSTANCE = new OrganisationUnitByLevelComparator();

    @Override
    public int compare(OrganisationUnit o1, OrganisationUnit o2) {
        return ((Integer) o1.getLevel()).compareTo(o2.getLevel());
    }
}
