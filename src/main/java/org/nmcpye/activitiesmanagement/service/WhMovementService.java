package org.nmcpye.activitiesmanagement.service;

import java.util.Optional;
import org.nmcpye.activitiesmanagement.domain.WhMovement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link WhMovement}.
 */
public interface WhMovementService {
    /**
     * Save a wHMovement.
     *
     * @param wHMovement the entity to save.
     * @return the persisted entity.
     */
    WhMovement save(WhMovement wHMovement);

    /**
     * Partially updates a wHMovement.
     *
     * @param wHMovement the entity to update partially.
     * @return the persisted entity.
     */
    Optional<WhMovement> partialUpdate(WhMovement wHMovement);

    /**
     * Get all the wHMovements.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<WhMovement> findAll(Pageable pageable);

    /**
     * Get the "id" wHMovement.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<WhMovement> findOne(Long id);

    /**
     * Delete the "id" wHMovement.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
