package org.nmcpye.activitiesmanagement.service;

import java.util.List;
import java.util.Optional;
import org.nmcpye.activitiesmanagement.domain.Period;

/**
 * Service Interface for managing {@link Period}.
 */
public interface PeriodService {
    /**
     * Save a period.
     *
     * @param period the entity to save.
     * @return the persisted entity.
     */
    Period save(Period period);

    /**
     * Partially updates a period.
     *
     * @param period the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Period> partialUpdate(Period period);

    /**
     * Get all the periods.
     *
     * @return the list of entities.
     */
    List<Period> findAll();

    /**
     * Get the "id" period.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Period> findOne(Long id);

    /**
     * Delete the "id" period.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
