package org.nmcpye.activitiesmanagement.service;

import org.nmcpye.activitiesmanagement.domain.ChvMalariaCaseReport;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link ChvMalariaCaseReport}.
 */
public interface ChvMalariaCaseReportService {
    /**
     * Save a chvMalariaCaseReport.
     *
     * @param chvMalariaCaseReport the entity to save.
     * @return the persisted entity.
     */
    ChvMalariaCaseReport save(ChvMalariaCaseReport chvMalariaCaseReport);

    /**
     * Partially updates a chvMalariaCaseReport.
     *
     * @param chvMalariaCaseReport the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ChvMalariaCaseReport> partialUpdate(ChvMalariaCaseReport chvMalariaCaseReport);

    /**
     * Get all the chvMalariaCaseReports.
     *
     * @return the list of entities.
     */
    List<ChvMalariaCaseReport> findAll();

    /**
     * Get the "id" chvMalariaCaseReport.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ChvMalariaCaseReport> findOne(Long id);

    /**
     * Delete the "id" chvMalariaCaseReport.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
