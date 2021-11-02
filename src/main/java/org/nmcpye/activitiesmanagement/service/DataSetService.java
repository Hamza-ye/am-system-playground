package org.nmcpye.activitiesmanagement.service;

import java.util.List;
import java.util.Optional;
import org.nmcpye.activitiesmanagement.domain.DataSet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link DataSet}.
 */
public interface DataSetService {
    /**
     * Save a dataSet.
     *
     * @param dataSet the entity to save.
     * @return the persisted entity.
     */
    DataSet save(DataSet dataSet);

    /**
     * Partially updates a dataSet.
     *
     * @param dataSet the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DataSet> partialUpdate(DataSet dataSet);

    /**
     * Get all the dataSets.
     *
     * @return the list of entities.
     */
    List<DataSet> findAll();

    /**
     * Get all the dataSets with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DataSet> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" dataSet.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DataSet> findOne(Long id);

    /**
     * Delete the "id" dataSet.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
