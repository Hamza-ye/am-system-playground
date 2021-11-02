package org.nmcpye.activitiesmanagement.service;

import java.util.List;
import java.util.Optional;
import org.nmcpye.activitiesmanagement.domain.LLINSFamilyReportHistory;

/**
 * Service Interface for managing {@link LLINSFamilyReportHistory}.
 */
public interface LLINSFamilyReportHistoryService {
    /**
     * Save a lLINSFamilyReportHistory.
     *
     * @param lLINSFamilyReportHistory the entity to save.
     * @return the persisted entity.
     */
    LLINSFamilyReportHistory save(LLINSFamilyReportHistory lLINSFamilyReportHistory);

    /**
     * Partially updates a lLINSFamilyReportHistory.
     *
     * @param lLINSFamilyReportHistory the entity to update partially.
     * @return the persisted entity.
     */
    Optional<LLINSFamilyReportHistory> partialUpdate(LLINSFamilyReportHistory lLINSFamilyReportHistory);

    /**
     * Get all the lLINSFamilyReportHistories.
     *
     * @return the list of entities.
     */
    List<LLINSFamilyReportHistory> findAll();

    /**
     * Get the "id" lLINSFamilyReportHistory.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<LLINSFamilyReportHistory> findOne(Long id);

    /**
     * Delete the "id" lLINSFamilyReportHistory.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
