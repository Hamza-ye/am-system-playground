package org.nmcpye.activitiesmanagement.service;

import java.util.Optional;
import org.nmcpye.activitiesmanagement.domain.LlinsFamilyReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link LlinsFamilyReport}.
 */
public interface LlinsFamilyReportService {
    /**
     * Save a lLINSFamilyReport.
     *
     * @param lLINSFamilyReport the entity to save.
     * @return the persisted entity.
     */
    LlinsFamilyReport save(LlinsFamilyReport lLINSFamilyReport);

    /**
     * Partially updates a lLINSFamilyReport.
     *
     * @param lLINSFamilyReport the entity to update partially.
     * @return the persisted entity.
     */
    Optional<LlinsFamilyReport> partialUpdate(LlinsFamilyReport lLINSFamilyReport);

    /**
     * Get all the lLINSFamilyReports.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<LlinsFamilyReport> findAll(Pageable pageable);

    /**
     * Get the "id" lLINSFamilyReport.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<LlinsFamilyReport> findOne(Long id);

    /**
     * Delete the "id" lLINSFamilyReport.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
