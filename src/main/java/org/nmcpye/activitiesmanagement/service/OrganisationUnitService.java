package org.nmcpye.activitiesmanagement.service;

import java.util.List;
import java.util.Optional;
import org.nmcpye.activitiesmanagement.domain.organisationunit.OrganisationUnit;

/**
 * Service Interface for managing {@link OrganisationUnit}.
 */
public interface OrganisationUnitService {
    /**
     * Save a organisationUnit.
     *
     * @param organisationUnit the entity to save.
     * @return the persisted entity.
     */
    OrganisationUnit save(OrganisationUnit organisationUnit);

    /**
     * Partially updates a organisationUnit.
     *
     * @param organisationUnit the entity to update partially.
     * @return the persisted entity.
     */
    Optional<OrganisationUnit> partialUpdate(OrganisationUnit organisationUnit);

    /**
     * Get all the organisationUnits.
     *
     * @return the list of entities.
     */
    List<OrganisationUnit> findAll();

    /**
     * Get the "id" organisationUnit.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OrganisationUnit> findOne(Long id);

    /**
     * Delete the "id" organisationUnit.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
