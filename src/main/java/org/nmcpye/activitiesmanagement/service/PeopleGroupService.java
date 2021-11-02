package org.nmcpye.activitiesmanagement.service;

import java.util.List;
import java.util.Optional;
import org.nmcpye.activitiesmanagement.domain.PeopleGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link PeopleGroup}.
 */
public interface PeopleGroupService {
    /**
     * Save a peopleGroup.
     *
     * @param peopleGroup the entity to save.
     * @return the persisted entity.
     */
    PeopleGroup save(PeopleGroup peopleGroup);

    /**
     * Partially updates a peopleGroup.
     *
     * @param peopleGroup the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PeopleGroup> partialUpdate(PeopleGroup peopleGroup);

    /**
     * Get all the peopleGroups.
     *
     * @return the list of entities.
     */
    List<PeopleGroup> findAll();

    /**
     * Get all the peopleGroups with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PeopleGroup> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" peopleGroup.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PeopleGroup> findOne(Long id);

    /**
     * Delete the "id" peopleGroup.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
