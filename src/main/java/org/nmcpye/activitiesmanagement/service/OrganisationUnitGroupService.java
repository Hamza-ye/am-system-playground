package org.nmcpye.activitiesmanagement.service;

import java.util.List;
import java.util.Optional;
import org.nmcpye.activitiesmanagement.domain.OrganisationUnitGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link OrganisationUnitGroup}.
 */
public interface OrganisationUnitGroupService {
    /**
     * Save a organisationUnitGroup.
     *
     * @param organisationUnitGroup the entity to save.
     * @return the persisted entity.
     */
    OrganisationUnitGroup save(OrganisationUnitGroup organisationUnitGroup);

    /**
     * Partially updates a organisationUnitGroup.
     *
     * @param organisationUnitGroup the entity to update partially.
     * @return the persisted entity.
     */
    Optional<OrganisationUnitGroup> partialUpdate(OrganisationUnitGroup organisationUnitGroup);

    /**
     * Get all the organisationUnitGroups.
     *
     * @return the list of entities.
     */
    List<OrganisationUnitGroup> findAll();

    /**
     * Get all the organisationUnitGroups with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<OrganisationUnitGroup> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" organisationUnitGroup.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OrganisationUnitGroup> findOne(Long id);

    /**
     * Delete the "id" organisationUnitGroup.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
