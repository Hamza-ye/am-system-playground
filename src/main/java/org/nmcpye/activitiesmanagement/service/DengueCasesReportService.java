package org.nmcpye.activitiesmanagement.service;

import org.nmcpye.activitiesmanagement.domain.dataset.DengueCasesReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link DengueCasesReport}.
 */
public interface DengueCasesReportService {
    /**
     * Save a dengueCasesReport.
     *
     * @param dengueCasesReport the entity to save.
     * @return the persisted entity.
     */
    DengueCasesReport save(DengueCasesReport dengueCasesReport);

    /**
     * Partially updates a dengueCasesReport.
     *
     * @param dengueCasesReport the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DengueCasesReport> partialUpdate(DengueCasesReport dengueCasesReport);

    /**
     * Get all the dengueCasesReports.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DengueCasesReport> findAll(Pageable pageable);

    /**
     * Get the "id" dengueCasesReport.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DengueCasesReport> findOne(Long id);

    /**
     * Delete the "id" dengueCasesReport.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
