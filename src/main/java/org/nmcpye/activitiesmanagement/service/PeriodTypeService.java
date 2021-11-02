package org.nmcpye.activitiesmanagement.service;

import java.util.List;
import java.util.Optional;
import org.nmcpye.activitiesmanagement.domain.PeriodType;

/**
 * Service Interface for managing {@link PeriodType}.
 */
public interface PeriodTypeService {
    /**
     * Save a periodType.
     *
     * @param periodType the entity to save.
     * @return the persisted entity.
     */
    PeriodType save(PeriodType periodType);

    /**
     * Partially updates a periodType.
     *
     * @param periodType the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PeriodType> partialUpdate(PeriodType periodType);

    /**
     * Get all the periodTypes.
     *
     * @return the list of entities.
     */
    List<PeriodType> findAll();

    /**
     * Get the "id" periodType.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PeriodType> findOne(Long id);

    /**
     * Delete the "id" periodType.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
