package org.nmcpye.activitiesmanagement.service;

import java.util.List;
import java.util.Optional;
import org.nmcpye.activitiesmanagement.domain.LlinsFamilyReportHistory;

/**
 * Service Interface for managing {@link LlinsFamilyReportHistory}.
 */
public interface LlinsFamilyReportHistoryService {
    /**
     * Save a lLINSFamilyReportHistory.
     *
     * @param lLINSFamilyReportHistory the entity to save.
     * @return the persisted entity.
     */
    LlinsFamilyReportHistory save(LlinsFamilyReportHistory lLINSFamilyReportHistory);

    /**
     * Partially updates a lLINSFamilyReportHistory.
     *
     * @param lLINSFamilyReportHistory the entity to update partially.
     * @return the persisted entity.
     */
    Optional<LlinsFamilyReportHistory> partialUpdate(LlinsFamilyReportHistory lLINSFamilyReportHistory);

    /**
     * Get all the lLINSFamilyReportHistories.
     *
     * @return the list of entities.
     */
    List<LlinsFamilyReportHistory> findAll();

    /**
     * Get the "id" lLINSFamilyReportHistory.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<LlinsFamilyReportHistory> findOne(Long id);

    /**
     * Delete the "id" lLINSFamilyReportHistory.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
