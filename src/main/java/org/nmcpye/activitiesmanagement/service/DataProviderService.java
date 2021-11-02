package org.nmcpye.activitiesmanagement.service;

import java.util.List;
import java.util.Optional;
import org.nmcpye.activitiesmanagement.domain.DataProvider;

/**
 * Service Interface for managing {@link DataProvider}.
 */
public interface DataProviderService {
    /**
     * Save a dataProvider.
     *
     * @param dataProvider the entity to save.
     * @return the persisted entity.
     */
    DataProvider save(DataProvider dataProvider);

    /**
     * Partially updates a dataProvider.
     *
     * @param dataProvider the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DataProvider> partialUpdate(DataProvider dataProvider);

    /**
     * Get all the dataProviders.
     *
     * @return the list of entities.
     */
    List<DataProvider> findAll();

    /**
     * Get the "id" dataProvider.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DataProvider> findOne(Long id);

    /**
     * Delete the "id" dataProvider.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
