package org.nmcpye.activitiesmanagement.service;

import java.util.List;
import java.util.Optional;
import org.nmcpye.activitiesmanagement.domain.RelativePeriods;

/**
 * Service Interface for managing {@link RelativePeriods}.
 */
public interface RelativePeriodsService {
    /**
     * Save a relativePeriods.
     *
     * @param relativePeriods the entity to save.
     * @return the persisted entity.
     */
    RelativePeriods save(RelativePeriods relativePeriods);

    /**
     * Partially updates a relativePeriods.
     *
     * @param relativePeriods the entity to update partially.
     * @return the persisted entity.
     */
    Optional<RelativePeriods> partialUpdate(RelativePeriods relativePeriods);

    /**
     * Get all the relativePeriods.
     *
     * @return the list of entities.
     */
    List<RelativePeriods> findAll();

    /**
     * Get the "id" relativePeriods.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<RelativePeriods> findOne(Long id);

    /**
     * Delete the "id" relativePeriods.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
