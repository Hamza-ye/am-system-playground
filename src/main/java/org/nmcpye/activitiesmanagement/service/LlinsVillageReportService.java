package org.nmcpye.activitiesmanagement.service;

import org.nmcpye.activitiesmanagement.domain.LlinsVillageReport;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link LlinsVillageReport}.
 */
public interface LlinsVillageReportService {
    /**
     * Save a llinsVillageReport.
     *
     * @param llinsVillageReport the entity to save.
     * @return the persisted entity.
     */
    LlinsVillageReport save(LlinsVillageReport llinsVillageReport);

    /**
     * Partially updates a llinsVillageReport.
     *
     * @param llinsVillageReport the entity to update partially.
     * @return the persisted entity.
     */
    Optional<LlinsVillageReport> partialUpdate(LlinsVillageReport llinsVillageReport);

    /**
     * Get all the llinsVillageReports.
     *
     * @return the list of entities.
     */
    List<LlinsVillageReport> findAll();

    /**
     * Get the "id" llinsVillageReport.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<LlinsVillageReport> findOne(Long id);

    /**
     * Delete the "id" llinsVillageReport.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
