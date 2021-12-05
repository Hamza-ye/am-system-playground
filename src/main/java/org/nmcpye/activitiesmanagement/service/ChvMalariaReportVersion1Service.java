package org.nmcpye.activitiesmanagement.service;

import org.nmcpye.activitiesmanagement.domain.ChvMalariaReportVersion1;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link ChvMalariaReportVersion1}.
 */
public interface ChvMalariaReportVersion1Service {
    /**
     * Save a chvMalariaReportVersion1.
     *
     * @param chvMalariaReportVersion1 the entity to save.
     * @return the persisted entity.
     */
    ChvMalariaReportVersion1 save(ChvMalariaReportVersion1 chvMalariaReportVersion1);

    /**
     * Partially updates a chvMalariaReportVersion1.
     *
     * @param chvMalariaReportVersion1 the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ChvMalariaReportVersion1> partialUpdate(ChvMalariaReportVersion1 chvMalariaReportVersion1);

    /**
     * Get all the chvMalariaReportVersion1s.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ChvMalariaReportVersion1> findAll(Pageable pageable);

    /**
     * Get the "id" chvMalariaReportVersion1.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ChvMalariaReportVersion1> findOne(Long id);

    /**
     * Delete the "id" chvMalariaReportVersion1.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
