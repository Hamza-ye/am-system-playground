package org.nmcpye.activitiesmanagement.service;

import java.util.List;
import java.util.Optional;
import org.nmcpye.activitiesmanagement.domain.LlinsVillageReportHistory;

/**
 * Service Interface for managing {@link LlinsVillageReportHistory}.
 */
public interface LlinsVillageReportHistoryService {
    /**
     * Save a lLINSVillageReportHistory.
     *
     * @param lLINSVillageReportHistory the entity to save.
     * @return the persisted entity.
     */
    LlinsVillageReportHistory save(LlinsVillageReportHistory lLINSVillageReportHistory);

    /**
     * Partially updates a lLINSVillageReportHistory.
     *
     * @param lLINSVillageReportHistory the entity to update partially.
     * @return the persisted entity.
     */
    Optional<LlinsVillageReportHistory> partialUpdate(LlinsVillageReportHistory lLINSVillageReportHistory);

    /**
     * Get all the lLINSVillageReportHistories.
     *
     * @return the list of entities.
     */
    List<LlinsVillageReportHistory> findAll();

    /**
     * Get the "id" lLINSVillageReportHistory.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<LlinsVillageReportHistory> findOne(Long id);

    /**
     * Delete the "id" lLINSVillageReportHistory.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
