package org.nmcpye.activitiesmanagement.service;

import org.nmcpye.activitiesmanagement.domain.LlinsFamilyReportHistory;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link LlinsFamilyReportHistory}.
 */
public interface LlinsFamilyReportHistoryService {
    /**
     * Save a llinsFamilyReportHistory.
     *
     * @param llinsFamilyReportHistory the entity to save.
     * @return the persisted entity.
     */
    LlinsFamilyReportHistory save(LlinsFamilyReportHistory llinsFamilyReportHistory);

    /**
     * Partially updates a llinsFamilyReportHistory.
     *
     * @param llinsFamilyReportHistory the entity to update partially.
     * @return the persisted entity.
     */
    Optional<LlinsFamilyReportHistory> partialUpdate(LlinsFamilyReportHistory llinsFamilyReportHistory);

    /**
     * Get all the llinsFamilyReportHistories.
     *
     * @return the list of entities.
     */
    List<LlinsFamilyReportHistory> findAll();

    /**
     * Get the "id" llinsFamilyReportHistory.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<LlinsFamilyReportHistory> findOne(Long id);

    /**
     * Delete the "id" llinsFamilyReportHistory.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
