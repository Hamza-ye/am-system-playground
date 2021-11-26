package org.nmcpye.activitiesmanagement.service;

import org.nmcpye.activitiesmanagement.domain.WhMovement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link WhMovement}.
 */
public interface WhMovementService {
    /**
     * Save a whMovement.
     *
     * @param whMovement the entity to save.
     * @return the persisted entity.
     */
    WhMovement save(WhMovement whMovement);

    /**
     * Partially updates a whMovement.
     *
     * @param whMovement the entity to update partially.
     * @return the persisted entity.
     */
    Optional<WhMovement> partialUpdate(WhMovement whMovement);

    /**
     * Get all the whMovements.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<WhMovement> findAll(Pageable pageable);

    /**
     * Get the "id" whMovement.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<WhMovement> findOne(Long id);

    /**
     * Delete the "id" whMovement.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
