package org.nmcpye.activitiesmanagement.service;

import java.util.Optional;
import org.nmcpye.activitiesmanagement.domain.FamilyHead;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link FamilyHead}.
 */
public interface FamilyHeadService {
    /**
     * Save a familyHead.
     *
     * @param familyHead the entity to save.
     * @return the persisted entity.
     */
    FamilyHead save(FamilyHead familyHead);

    /**
     * Partially updates a familyHead.
     *
     * @param familyHead the entity to update partially.
     * @return the persisted entity.
     */
    Optional<FamilyHead> partialUpdate(FamilyHead familyHead);

    /**
     * Get all the familyHeads.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FamilyHead> findAll(Pageable pageable);

    /**
     * Get the "id" familyHead.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FamilyHead> findOne(Long id);

    /**
     * Delete the "id" familyHead.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
