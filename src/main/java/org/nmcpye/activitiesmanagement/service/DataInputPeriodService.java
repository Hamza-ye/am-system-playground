package org.nmcpye.activitiesmanagement.service;

import java.util.List;
import java.util.Optional;
import org.nmcpye.activitiesmanagement.domain.DataInputPeriod;

/**
 * Service Interface for managing {@link DataInputPeriod}.
 */
public interface DataInputPeriodService {
    /**
     * Save a dataInputPeriod.
     *
     * @param dataInputPeriod the entity to save.
     * @return the persisted entity.
     */
    DataInputPeriod save(DataInputPeriod dataInputPeriod);

    /**
     * Partially updates a dataInputPeriod.
     *
     * @param dataInputPeriod the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DataInputPeriod> partialUpdate(DataInputPeriod dataInputPeriod);

    /**
     * Get all the dataInputPeriods.
     *
     * @return the list of entities.
     */
    List<DataInputPeriod> findAll();

    /**
     * Get the "id" dataInputPeriod.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DataInputPeriod> findOne(Long id);

    /**
     * Delete the "id" dataInputPeriod.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
