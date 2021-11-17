package org.nmcpye.activitiesmanagement.service;

import java.util.List;
import java.util.Optional;
import org.nmcpye.activitiesmanagement.domain.chv.CHV;

/**
 * Service Interface for managing {@link CHV}.
 */
public interface CHVService {
    /**
     * Save a cHV.
     *
     * @param cHV the entity to save.
     * @return the persisted entity.
     */
    CHV save(CHV cHV);

    /**
     * Partially updates a cHV.
     *
     * @param cHV the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CHV> partialUpdate(CHV cHV);

    /**
     * Get all the cHVS.
     *
     * @return the list of entities.
     */
    List<CHV> findAll();

    /**
     * Get the "id" cHV.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CHV> findOne(Long id);

    /**
     * Delete the "id" cHV.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
