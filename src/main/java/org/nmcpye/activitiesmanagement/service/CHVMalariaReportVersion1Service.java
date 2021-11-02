package org.nmcpye.activitiesmanagement.service;

import java.util.List;
import java.util.Optional;
import org.nmcpye.activitiesmanagement.domain.CHVMalariaReportVersion1;

/**
 * Service Interface for managing {@link CHVMalariaReportVersion1}.
 */
public interface CHVMalariaReportVersion1Service {
    /**
     * Save a cHVMalariaReportVersion1.
     *
     * @param cHVMalariaReportVersion1 the entity to save.
     * @return the persisted entity.
     */
    CHVMalariaReportVersion1 save(CHVMalariaReportVersion1 cHVMalariaReportVersion1);

    /**
     * Partially updates a cHVMalariaReportVersion1.
     *
     * @param cHVMalariaReportVersion1 the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CHVMalariaReportVersion1> partialUpdate(CHVMalariaReportVersion1 cHVMalariaReportVersion1);

    /**
     * Get all the cHVMalariaReportVersion1s.
     *
     * @return the list of entities.
     */
    List<CHVMalariaReportVersion1> findAll();

    /**
     * Get the "id" cHVMalariaReportVersion1.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CHVMalariaReportVersion1> findOne(Long id);

    /**
     * Delete the "id" cHVMalariaReportVersion1.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
