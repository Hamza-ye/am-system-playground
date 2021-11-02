package org.nmcpye.activitiesmanagement.service;

import java.util.List;
import java.util.Optional;
import org.nmcpye.activitiesmanagement.domain.DemographicDataSource;

/**
 * Service Interface for managing {@link DemographicDataSource}.
 */
public interface DemographicDataSourceService {
    /**
     * Save a demographicDataSource.
     *
     * @param demographicDataSource the entity to save.
     * @return the persisted entity.
     */
    DemographicDataSource save(DemographicDataSource demographicDataSource);

    /**
     * Partially updates a demographicDataSource.
     *
     * @param demographicDataSource the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DemographicDataSource> partialUpdate(DemographicDataSource demographicDataSource);

    /**
     * Get all the demographicDataSources.
     *
     * @return the list of entities.
     */
    List<DemographicDataSource> findAll();

    /**
     * Get the "id" demographicDataSource.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DemographicDataSource> findOne(Long id);

    /**
     * Delete the "id" demographicDataSource.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
