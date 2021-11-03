package org.nmcpye.activitiesmanagement.service;

import java.util.List;
import java.util.Optional;
import org.nmcpye.activitiesmanagement.domain.organisationunit.OrganisationUnitGroupSet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link OrganisationUnitGroupSet}.
 */
public interface OrganisationUnitGroupSetService {
    /**
     * Save a organisationUnitGroupSet.
     *
     * @param organisationUnitGroupSet the entity to save.
     * @return the persisted entity.
     */
    OrganisationUnitGroupSet save(OrganisationUnitGroupSet organisationUnitGroupSet);

    /**
     * Partially updates a organisationUnitGroupSet.
     *
     * @param organisationUnitGroupSet the entity to update partially.
     * @return the persisted entity.
     */
    Optional<OrganisationUnitGroupSet> partialUpdate(OrganisationUnitGroupSet organisationUnitGroupSet);

    /**
     * Get all the organisationUnitGroupSets.
     *
     * @return the list of entities.
     */
    List<OrganisationUnitGroupSet> findAll();

    /**
     * Get all the organisationUnitGroupSets with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<OrganisationUnitGroupSet> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" organisationUnitGroupSet.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OrganisationUnitGroupSet> findOne(Long id);

    /**
     * Delete the "id" organisationUnitGroupSet.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
