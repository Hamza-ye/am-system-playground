package org.nmcpye.activitiesmanagement.service;

import java.util.List;
import java.util.Optional;
import org.nmcpye.activitiesmanagement.domain.DemographicData;

/**
 * Service Interface for managing {@link DemographicData}.
 */
public interface DemographicDataService {
    /**
     * Save a demographicData.
     *
     * @param demographicData the entity to save.
     * @return the persisted entity.
     */
    DemographicData save(DemographicData demographicData);

    /**
     * Partially updates a demographicData.
     *
     * @param demographicData the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DemographicData> partialUpdate(DemographicData demographicData);

    /**
     * Get all the demographicData.
     *
     * @return the list of entities.
     */
    List<DemographicData> findAll();

    /**
     * Get the "id" demographicData.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DemographicData> findOne(Long id);

    /**
     * Delete the "id" demographicData.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
