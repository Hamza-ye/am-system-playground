package org.nmcpye.activitiesmanagement.service;

import java.util.Optional;
import org.nmcpye.activitiesmanagement.domain.LLINSFamilyReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link LLINSFamilyReport}.
 */
public interface LLINSFamilyReportService {
    /**
     * Save a lLINSFamilyReport.
     *
     * @param lLINSFamilyReport the entity to save.
     * @return the persisted entity.
     */
    LLINSFamilyReport save(LLINSFamilyReport lLINSFamilyReport);

    /**
     * Partially updates a lLINSFamilyReport.
     *
     * @param lLINSFamilyReport the entity to update partially.
     * @return the persisted entity.
     */
    Optional<LLINSFamilyReport> partialUpdate(LLINSFamilyReport lLINSFamilyReport);

    /**
     * Get all the lLINSFamilyReports.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<LLINSFamilyReport> findAll(Pageable pageable);

    /**
     * Get the "id" lLINSFamilyReport.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<LLINSFamilyReport> findOne(Long id);

    /**
     * Delete the "id" lLINSFamilyReport.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
