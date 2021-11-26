package org.nmcpye.activitiesmanagement.service;

import org.nmcpye.activitiesmanagement.domain.LlinsFamilyReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link LlinsFamilyReport}.
 */
public interface LlinsFamilyReportService {
    /**
     * Save a llinsFamilyReport.
     *
     * @param llinsFamilyReport the entity to save.
     * @return the persisted entity.
     */
    LlinsFamilyReport save(LlinsFamilyReport llinsFamilyReport);

    /**
     * Partially updates a llinsFamilyReport.
     *
     * @param llinsFamilyReport the entity to update partially.
     * @return the persisted entity.
     */
    Optional<LlinsFamilyReport> partialUpdate(LlinsFamilyReport llinsFamilyReport);

    /**
     * Get all the llinsFamilyReports.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<LlinsFamilyReport> findAll(Pageable pageable);

    /**
     * Get the "id" llinsFamilyReport.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<LlinsFamilyReport> findOne(Long id);

    /**
     * Delete the "id" llinsFamilyReport.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
