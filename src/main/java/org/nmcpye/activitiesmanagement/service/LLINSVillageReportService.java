package org.nmcpye.activitiesmanagement.service;

import java.util.List;
import java.util.Optional;
import org.nmcpye.activitiesmanagement.domain.LLINSVillageReport;

/**
 * Service Interface for managing {@link LLINSVillageReport}.
 */
public interface LLINSVillageReportService {
    /**
     * Save a lLINSVillageReport.
     *
     * @param lLINSVillageReport the entity to save.
     * @return the persisted entity.
     */
    LLINSVillageReport save(LLINSVillageReport lLINSVillageReport);

    /**
     * Partially updates a lLINSVillageReport.
     *
     * @param lLINSVillageReport the entity to update partially.
     * @return the persisted entity.
     */
    Optional<LLINSVillageReport> partialUpdate(LLINSVillageReport lLINSVillageReport);

    /**
     * Get all the lLINSVillageReports.
     *
     * @return the list of entities.
     */
    List<LLINSVillageReport> findAll();

    /**
     * Get the "id" lLINSVillageReport.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<LLINSVillageReport> findOne(Long id);

    /**
     * Delete the "id" lLINSVillageReport.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
