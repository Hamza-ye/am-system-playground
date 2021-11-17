package org.nmcpye.activitiesmanagement.service;

import java.util.List;
import java.util.Optional;
import org.nmcpye.activitiesmanagement.domain.dataset.MalariaCasesReport;

/**
 * Service Interface for managing {@link MalariaCasesReport}.
 */
public interface MalariaCasesReportService {
    /**
     * Save a malariaCasesReport.
     *
     * @param malariaCasesReport the entity to save.
     * @return the persisted entity.
     */
    MalariaCasesReport save(MalariaCasesReport malariaCasesReport);

    /**
     * Partially updates a malariaCasesReport.
     *
     * @param malariaCasesReport the entity to update partially.
     * @return the persisted entity.
     */
    Optional<MalariaCasesReport> partialUpdate(MalariaCasesReport malariaCasesReport);

    /**
     * Get all the malariaCasesReports.
     *
     * @return the list of entities.
     */
    List<MalariaCasesReport> findAll();

    /**
     * Get the "id" malariaCasesReport.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MalariaCasesReport> findOne(Long id);

    /**
     * Delete the "id" malariaCasesReport.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
