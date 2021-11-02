package org.nmcpye.activitiesmanagement.service;

import java.util.List;
import java.util.Optional;
import org.nmcpye.activitiesmanagement.domain.LLINSVillageReportHistory;

/**
 * Service Interface for managing {@link LLINSVillageReportHistory}.
 */
public interface LLINSVillageReportHistoryService {
    /**
     * Save a lLINSVillageReportHistory.
     *
     * @param lLINSVillageReportHistory the entity to save.
     * @return the persisted entity.
     */
    LLINSVillageReportHistory save(LLINSVillageReportHistory lLINSVillageReportHistory);

    /**
     * Partially updates a lLINSVillageReportHistory.
     *
     * @param lLINSVillageReportHistory the entity to update partially.
     * @return the persisted entity.
     */
    Optional<LLINSVillageReportHistory> partialUpdate(LLINSVillageReportHistory lLINSVillageReportHistory);

    /**
     * Get all the lLINSVillageReportHistories.
     *
     * @return the list of entities.
     */
    List<LLINSVillageReportHistory> findAll();

    /**
     * Get the "id" lLINSVillageReportHistory.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<LLINSVillageReportHistory> findOne(Long id);

    /**
     * Delete the "id" lLINSVillageReportHistory.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
