package org.nmcpye.activitiesmanagement.service;

import org.nmcpye.activitiesmanagement.domain.fileresource.ExternalFileResource;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link ExternalFileResource}.
 */
public interface ExternalFileResourceService {
    /**
     * Save a externalFileResource.
     *
     * @param externalFileResource the entity to save.
     * @return the persisted entity.
     */
    ExternalFileResource save(ExternalFileResource externalFileResource);

    /**
     * Partially updates a externalFileResource.
     *
     * @param externalFileResource the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ExternalFileResource> partialUpdate(ExternalFileResource externalFileResource);

    /**
     * Get all the externalFileResources.
     *
     * @return the list of entities.
     */
    List<ExternalFileResource> findAll();

    /**
     * Get the "id" externalFileResource.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ExternalFileResource> findOne(Long id);

    /**
     * Delete the "id" externalFileResource.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
