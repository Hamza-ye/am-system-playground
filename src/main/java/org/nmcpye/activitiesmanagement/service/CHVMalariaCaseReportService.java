package org.nmcpye.activitiesmanagement.service;

import java.util.List;
import java.util.Optional;
import org.nmcpye.activitiesmanagement.domain.CHVMalariaCaseReport;

/**
 * Service Interface for managing {@link CHVMalariaCaseReport}.
 */
public interface CHVMalariaCaseReportService {
    /**
     * Save a cHVMalariaCaseReport.
     *
     * @param cHVMalariaCaseReport the entity to save.
     * @return the persisted entity.
     */
    CHVMalariaCaseReport save(CHVMalariaCaseReport cHVMalariaCaseReport);

    /**
     * Partially updates a cHVMalariaCaseReport.
     *
     * @param cHVMalariaCaseReport the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CHVMalariaCaseReport> partialUpdate(CHVMalariaCaseReport cHVMalariaCaseReport);

    /**
     * Get all the cHVMalariaCaseReports.
     *
     * @return the list of entities.
     */
    List<CHVMalariaCaseReport> findAll();

    /**
     * Get the "id" cHVMalariaCaseReport.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CHVMalariaCaseReport> findOne(Long id);

    /**
     * Delete the "id" cHVMalariaCaseReport.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
