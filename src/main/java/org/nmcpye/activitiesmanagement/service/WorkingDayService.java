package org.nmcpye.activitiesmanagement.service;

import java.util.List;
import java.util.Optional;
import org.nmcpye.activitiesmanagement.domain.WorkingDay;

/**
 * Service Interface for managing {@link WorkingDay}.
 */
public interface WorkingDayService {
    /**
     * Save a workingDay.
     *
     * @param workingDay the entity to save.
     * @return the persisted entity.
     */
    WorkingDay save(WorkingDay workingDay);

    /**
     * Partially updates a workingDay.
     *
     * @param workingDay the entity to update partially.
     * @return the persisted entity.
     */
    Optional<WorkingDay> partialUpdate(WorkingDay workingDay);

    /**
     * Get all the workingDays.
     *
     * @return the list of entities.
     */
    List<WorkingDay> findAll();

    /**
     * Get the "id" workingDay.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<WorkingDay> findOne(Long id);

    /**
     * Delete the "id" workingDay.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
