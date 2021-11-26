package org.nmcpye.activitiesmanagement.service;

import java.util.List;
import java.util.Optional;
import org.nmcpye.activitiesmanagement.domain.ChvMalariaCaseReport;

/**
 * Service Interface for managing {@link ChvMalariaCaseReport}.
 */
public interface ChvMalariaCaseReportService {
    /**
     * Save a cHVMalariaCaseReport.
     *
     * @param cHVMalariaCaseReport the entity to save.
     * @return the persisted entity.
     */
    ChvMalariaCaseReport save(ChvMalariaCaseReport cHVMalariaCaseReport);

    /**
     * Partially updates a cHVMalariaCaseReport.
     *
     * @param cHVMalariaCaseReport the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ChvMalariaCaseReport> partialUpdate(ChvMalariaCaseReport cHVMalariaCaseReport);

    /**
     * Get all the cHVMalariaCaseReports.
     *
     * @return the list of entities.
     */
    List<ChvMalariaCaseReport> findAll();

    /**
     * Get the "id" cHVMalariaCaseReport.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ChvMalariaCaseReport> findOne(Long id);

    /**
     * Delete the "id" cHVMalariaCaseReport.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
