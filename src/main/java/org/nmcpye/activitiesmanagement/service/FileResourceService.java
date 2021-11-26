package org.nmcpye.activitiesmanagement.service;

import org.nmcpye.activitiesmanagement.domain.fileresource.FileResource;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link FileResource}.
 */
public interface FileResourceService {
    /**
     * Save a fileResource.
     *
     * @param fileResource the entity to save.
     * @return the persisted entity.
     */
    FileResource save(FileResource fileResource);

    /**
     * Partially updates a fileResource.
     *
     * @param fileResource the entity to update partially.
     * @return the persisted entity.
     */
    Optional<FileResource> partialUpdate(FileResource fileResource);

    /**
     * Get all the fileResources.
     *
     * @return the list of entities.
     */
    List<FileResource> findAll();

    /**
     * Get the "id" fileResource.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FileResource> findOne(Long id);

    /**
     * Delete the "id" fileResource.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
