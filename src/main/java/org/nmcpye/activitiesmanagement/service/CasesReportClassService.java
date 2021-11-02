package org.nmcpye.activitiesmanagement.service;

import java.util.List;
import java.util.Optional;
import org.nmcpye.activitiesmanagement.domain.CasesReportClass;

/**
 * Service Interface for managing {@link CasesReportClass}.
 */
public interface CasesReportClassService {
    /**
     * Save a casesReportClass.
     *
     * @param casesReportClass the entity to save.
     * @return the persisted entity.
     */
    CasesReportClass save(CasesReportClass casesReportClass);

    /**
     * Partially updates a casesReportClass.
     *
     * @param casesReportClass the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CasesReportClass> partialUpdate(CasesReportClass casesReportClass);

    /**
     * Get all the casesReportClasses.
     *
     * @return the list of entities.
     */
    List<CasesReportClass> findAll();

    /**
     * Get the "id" casesReportClass.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CasesReportClass> findOne(Long id);

    /**
     * Delete the "id" casesReportClass.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
