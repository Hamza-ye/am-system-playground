package org.nmcpye.activitiesmanagement.service;

import org.nmcpye.activitiesmanagement.domain.LlinsFamilyTarget;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link LlinsFamilyTarget}.
 */
public interface LlinsFamilyTargetService {
    /**
     * Save a llinsFamilyTarget.
     *
     * @param llinsFamilyTarget the entity to save.
     * @return the persisted entity.
     */
    LlinsFamilyTarget save(LlinsFamilyTarget llinsFamilyTarget);

    /**
     * Partially updates a llinsFamilyTarget.
     *
     * @param llinsFamilyTarget the entity to update partially.
     * @return the persisted entity.
     */
    Optional<LlinsFamilyTarget> partialUpdate(LlinsFamilyTarget llinsFamilyTarget);

    /**
     * Get all the llinsFamilyTargets.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<LlinsFamilyTarget> findAll(Pageable pageable);

    /**
     * Get the "id" llinsFamilyTarget.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<LlinsFamilyTarget> findOne(Long id);

    /**
     * Delete the "id" llinsFamilyTarget.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
