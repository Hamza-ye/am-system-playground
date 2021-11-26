package org.nmcpye.activitiesmanagement.service;

import java.util.Optional;
import org.nmcpye.activitiesmanagement.domain.LlinsVillageTarget;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link LlinsVillageTarget}.
 */
public interface LlinsVillageTargetService {
    /**
     * Save a lLINSVillageTarget.
     *
     * @param lLINSVillageTarget the entity to save.
     * @return the persisted entity.
     */
    LlinsVillageTarget save(LlinsVillageTarget lLINSVillageTarget);

    /**
     * Partially updates a lLINSVillageTarget.
     *
     * @param lLINSVillageTarget the entity to update partially.
     * @return the persisted entity.
     */
    Optional<LlinsVillageTarget> partialUpdate(LlinsVillageTarget lLINSVillageTarget);

    /**
     * Get all the lLINSVillageTargets.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<LlinsVillageTarget> findAll(Pageable pageable);

    /**
     * Get the "id" lLINSVillageTarget.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<LlinsVillageTarget> findOne(Long id);

    /**
     * Delete the "id" lLINSVillageTarget.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
