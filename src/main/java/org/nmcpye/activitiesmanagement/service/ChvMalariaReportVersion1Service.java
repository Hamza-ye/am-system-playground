package org.nmcpye.activitiesmanagement.service;

import java.util.List;
import java.util.Optional;
import org.nmcpye.activitiesmanagement.domain.ChvMalariaReportVersion1;

/**
 * Service Interface for managing {@link ChvMalariaReportVersion1}.
 */
public interface ChvMalariaReportVersion1Service {
    /**
     * Save a cHVMalariaReportVersion1.
     *
     * @param cHVMalariaReportVersion1 the entity to save.
     * @return the persisted entity.
     */
    ChvMalariaReportVersion1 save(ChvMalariaReportVersion1 cHVMalariaReportVersion1);

    /**
     * Partially updates a cHVMalariaReportVersion1.
     *
     * @param cHVMalariaReportVersion1 the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ChvMalariaReportVersion1> partialUpdate(ChvMalariaReportVersion1 cHVMalariaReportVersion1);

    /**
     * Get all the cHVMalariaReportVersion1s.
     *
     * @return the list of entities.
     */
    List<ChvMalariaReportVersion1> findAll();

    /**
     * Get the "id" cHVMalariaReportVersion1.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ChvMalariaReportVersion1> findOne(Long id);

    /**
     * Delete the "id" cHVMalariaReportVersion1.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
