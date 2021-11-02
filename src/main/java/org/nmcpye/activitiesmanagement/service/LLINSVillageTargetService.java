package org.nmcpye.activitiesmanagement.service;

import java.util.Optional;
import org.nmcpye.activitiesmanagement.domain.LLINSVillageTarget;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link LLINSVillageTarget}.
 */
public interface LLINSVillageTargetService {
    /**
     * Save a lLINSVillageTarget.
     *
     * @param lLINSVillageTarget the entity to save.
     * @return the persisted entity.
     */
    LLINSVillageTarget save(LLINSVillageTarget lLINSVillageTarget);

    /**
     * Partially updates a lLINSVillageTarget.
     *
     * @param lLINSVillageTarget the entity to update partially.
     * @return the persisted entity.
     */
    Optional<LLINSVillageTarget> partialUpdate(LLINSVillageTarget lLINSVillageTarget);

    /**
     * Get all the lLINSVillageTargets.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<LLINSVillageTarget> findAll(Pageable pageable);

    /**
     * Get the "id" lLINSVillageTarget.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<LLINSVillageTarget> findOne(Long id);

    /**
     * Delete the "id" lLINSVillageTarget.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
