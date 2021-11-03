package org.nmcpye.activitiesmanagement.extended.organisationunit;

import org.nmcpye.activitiesmanagement.domain.organisationunit.OrganisationUnitLevel;
import org.nmcpye.activitiesmanagement.extended.common.IdentifiableObjectStore;

/**
 * Defines methods for persisting OrganisationUnitLevels.
 *
 */
public interface OrganisationUnitLevelStore extends IdentifiableObjectStore<OrganisationUnitLevel> {
    String ID = OrganisationUnitLevelStore.class.getName();

    /**
     * Deletes all OrganisationUnitLevels.
     */
    void deleteAll();

    /**
     * Gets the OrganisationUnitLevel at the given level.
     *
     * @param level the level.
     * @return the OrganisationUnitLevel at the given level.
     */
    OrganisationUnitLevel getByLevel(int level);
}
