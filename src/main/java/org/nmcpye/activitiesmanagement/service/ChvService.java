package org.nmcpye.activitiesmanagement.service;

import java.util.List;
import java.util.Optional;
import org.nmcpye.activitiesmanagement.domain.chv.Chv;

/**
 * Service Interface for managing {@link Chv}.
 */
public interface ChvService {
    /**
     * Save a cHV.
     *
     * @param cHV the entity to save.
     * @return the persisted entity.
     */
    Chv save(Chv cHV);

    /**
     * Partially updates a cHV.
     *
     * @param cHV the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Chv> partialUpdate(Chv cHV);

    /**
     * Get all the cHVS.
     *
     * @return the list of entities.
     */
    List<Chv> findAll();

    /**
     * Get the "id" cHV.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Chv> findOne(Long id);

    /**
     * Delete the "id" cHV.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
