package org.nmcpye.activitiesmanagement.extended.organisationunit.comparator;

import org.nmcpye.activitiesmanagement.domain.organisationunit.OrganisationUnitLevel;

import java.util.Comparator;

public class OrganisationUnitLevelComparator implements Comparator<OrganisationUnitLevel> {

    public static final Comparator<OrganisationUnitLevel> INSTANCE = new OrganisationUnitLevelComparator();

    @Override
    public int compare(OrganisationUnitLevel level1, OrganisationUnitLevel level2) {
        return level1.getLevel() - level2.getLevel();
    }
}
