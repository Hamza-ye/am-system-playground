package org.nmcpye.activitiesmanagement.service;

import java.util.List;
import java.util.Optional;
import org.nmcpye.activitiesmanagement.domain.LlinsVillageReport;

/**
 * Service Interface for managing {@link LlinsVillageReport}.
 */
public interface LlinsVillageReportService {
    /**
     * Save a lLINSVillageReport.
     *
     * @param lLINSVillageReport the entity to save.
     * @return the persisted entity.
     */
    LlinsVillageReport save(LlinsVillageReport lLINSVillageReport);

    /**
     * Partially updates a lLINSVillageReport.
     *
     * @param lLINSVillageReport the entity to update partially.
     * @return the persisted entity.
     */
    Optional<LlinsVillageReport> partialUpdate(LlinsVillageReport lLINSVillageReport);

    /**
     * Get all the lLINSVillageReports.
     *
     * @return the list of entities.
     */
    List<LlinsVillageReport> findAll();

    /**
     * Get the "id" lLINSVillageReport.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<LlinsVillageReport> findOne(Long id);

    /**
     * Delete the "id" lLINSVillageReport.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
