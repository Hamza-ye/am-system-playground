package org.nmcpye.activitiesmanagement.service;

import org.nmcpye.activitiesmanagement.domain.ImageAlbum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link ImageAlbum}.
 */
public interface ImageAlbumService {
    /**
     * Save a imageAlbum.
     *
     * @param imageAlbum the entity to save.
     * @return the persisted entity.
     */
    ImageAlbum save(ImageAlbum imageAlbum);

    /**
     * Partially updates a imageAlbum.
     *
     * @param imageAlbum the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ImageAlbum> partialUpdate(ImageAlbum imageAlbum);

    /**
     * Get all the imageAlbums.
     *
     * @return the list of entities.
     */
    List<ImageAlbum> findAll();

    /**
     * Get all the imageAlbums with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ImageAlbum> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" imageAlbum.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ImageAlbum> findOne(Long id);

    /**
     * Delete the "id" imageAlbum.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
