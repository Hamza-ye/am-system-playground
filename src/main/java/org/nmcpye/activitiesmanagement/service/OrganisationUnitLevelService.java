package org.nmcpye.activitiesmanagement.service;

import java.util.List;
import java.util.Optional;
import org.nmcpye.activitiesmanagement.domain.OrganisationUnitLevel;

/**
 * Service Interface for managing {@link OrganisationUnitLevel}.
 */
public interface OrganisationUnitLevelService {
    /**
     * Save a organisationUnitLevel.
     *
     * @param organisationUnitLevel the entity to save.
     * @return the persisted entity.
     */
    OrganisationUnitLevel save(OrganisationUnitLevel organisationUnitLevel);

    /**
     * Partially updates a organisationUnitLevel.
     *
     * @param organisationUnitLevel the entity to update partially.
     * @return the persisted entity.
     */
    Optional<OrganisationUnitLevel> partialUpdate(OrganisationUnitLevel organisationUnitLevel);

    /**
     * Get all the organisationUnitLevels.
     *
     * @return the list of entities.
     */
    List<OrganisationUnitLevel> findAll();

    /**
     * Get the "id" organisationUnitLevel.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OrganisationUnitLevel> findOne(Long id);

    /**
     * Delete the "id" organisationUnitLevel.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
