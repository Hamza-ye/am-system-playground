package org.nmcpye.activitiesmanagement.service;

import org.nmcpye.activitiesmanagement.domain.LlinsVillageReportHistory;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link LlinsVillageReportHistory}.
 */
public interface LlinsVillageReportHistoryService {
    /**
     * Save a llinsVillageReportHistory.
     *
     * @param llinsVillageReportHistory the entity to save.
     * @return the persisted entity.
     */
    LlinsVillageReportHistory save(LlinsVillageReportHistory llinsVillageReportHistory);

    /**
     * Partially updates a llinsVillageReportHistory.
     *
     * @param llinsVillageReportHistory the entity to update partially.
     * @return the persisted entity.
     */
    Optional<LlinsVillageReportHistory> partialUpdate(LlinsVillageReportHistory llinsVillageReportHistory);

    /**
     * Get all the llinsVillageReportHistories.
     *
     * @return the list of entities.
     */
    List<LlinsVillageReportHistory> findAll();

    /**
     * Get the "id" llinsVillageReportHistory.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<LlinsVillageReportHistory> findOne(Long id);

    /**
     * Delete the "id" llinsVillageReportHistory.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
