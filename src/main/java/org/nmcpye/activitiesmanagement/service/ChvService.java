package org.nmcpye.activitiesmanagement.service;

import org.nmcpye.activitiesmanagement.domain.chv.Chv;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link Chv}.
 */
public interface ChvService {
    /**
     * Save a chv.
     *
     * @param chv the entity to save.
     * @return the persisted entity.
     */
    Chv save(Chv chv);

    /**
     * Partially updates a chv.
     *
     * @param chv the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Chv> partialUpdate(Chv chv);

    /**
     * Get all the chvs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Chv> findAll(Pageable pageable);

    /**
     * Get the "id" chv.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Chv> findOne(Long id);

    /**
     * Delete the "id" chv.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
