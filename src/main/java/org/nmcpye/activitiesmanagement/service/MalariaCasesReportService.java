package org.nmcpye.activitiesmanagement.service;

import org.nmcpye.activitiesmanagement.domain.dataset.MalariaCasesReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

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
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MalariaCasesReport> findAll(Pageable pageable);

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
