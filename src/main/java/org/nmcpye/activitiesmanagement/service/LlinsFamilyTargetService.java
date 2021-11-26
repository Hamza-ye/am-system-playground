package org.nmcpye.activitiesmanagement.service;

import java.util.Optional;
import org.nmcpye.activitiesmanagement.domain.LlinsFamilyTarget;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link LlinsFamilyTarget}.
 */
public interface LlinsFamilyTargetService {
    /**
     * Save a lLINSFamilyTarget.
     *
     * @param lLINSFamilyTarget the entity to save.
     * @return the persisted entity.
     */
    LlinsFamilyTarget save(LlinsFamilyTarget lLINSFamilyTarget);

    /**
     * Partially updates a lLINSFamilyTarget.
     *
     * @param lLINSFamilyTarget the entity to update partially.
     * @return the persisted entity.
     */
    Optional<LlinsFamilyTarget> partialUpdate(LlinsFamilyTarget lLINSFamilyTarget);

    /**
     * Get all the lLINSFamilyTargets.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<LlinsFamilyTarget> findAll(Pageable pageable);

    /**
     * Get the "id" lLINSFamilyTarget.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<LlinsFamilyTarget> findOne(Long id);

    /**
     * Delete the "id" lLINSFamilyTarget.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
