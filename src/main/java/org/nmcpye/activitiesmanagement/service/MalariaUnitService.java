package org.nmcpye.activitiesmanagement.service;

import java.util.List;
import java.util.Optional;
import org.nmcpye.activitiesmanagement.domain.MalariaUnit;

/**
 * Service Interface for managing {@link MalariaUnit}.
 */
public interface MalariaUnitService {
    /**
     * Save a malariaUnit.
     *
     * @param malariaUnit the entity to save.
     * @return the persisted entity.
     */
    MalariaUnit save(MalariaUnit malariaUnit);

    /**
     * Partially updates a malariaUnit.
     *
     * @param malariaUnit the entity to update partially.
     * @return the persisted entity.
     */
    Optional<MalariaUnit> partialUpdate(MalariaUnit malariaUnit);

    /**
     * Get all the malariaUnits.
     *
     * @return the list of entities.
     */
    List<MalariaUnit> findAll();

    /**
     * Get the "id" malariaUnit.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MalariaUnit> findOne(Long id);

    /**
     * Delete the "id" malariaUnit.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
