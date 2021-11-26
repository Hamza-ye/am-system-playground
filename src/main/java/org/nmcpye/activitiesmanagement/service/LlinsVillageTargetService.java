package org.nmcpye.activitiesmanagement.service;

import org.nmcpye.activitiesmanagement.domain.LlinsVillageTarget;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link LlinsVillageTarget}.
 */
public interface LlinsVillageTargetService {
    /**
     * Save a llinsVillageTarget.
     *
     * @param llinsVillageTarget the entity to save.
     * @return the persisted entity.
     */
    LlinsVillageTarget save(LlinsVillageTarget llinsVillageTarget);

    /**
     * Partially updates a llinsVillageTarget.
     *
     * @param llinsVillageTarget the entity to update partially.
     * @return the persisted entity.
     */
    Optional<LlinsVillageTarget> partialUpdate(LlinsVillageTarget llinsVillageTarget);

    /**
     * Get all the llinsVillageTargets.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<LlinsVillageTarget> findAll(Pageable pageable);

    /**
     * Get the "id" llinsVillageTarget.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<LlinsVillageTarget> findOne(Long id);

    /**
     * Delete the "id" llinsVillageTarget.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
