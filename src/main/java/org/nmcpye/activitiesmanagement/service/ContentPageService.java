package org.nmcpye.activitiesmanagement.service;

import org.nmcpye.activitiesmanagement.domain.ContentPage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link ContentPage}.
 */
public interface ContentPageService {
    /**
     * Save a contentPage.
     *
     * @param contentPage the entity to save.
     * @return the persisted entity.
     */
    ContentPage save(ContentPage contentPage);

    /**
     * Partially updates a contentPage.
     *
     * @param contentPage the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ContentPage> partialUpdate(ContentPage contentPage);

    /**
     * Get all the contentPages.
     *
     * @return the list of entities.
     */
    List<ContentPage> findAll();

    /**
     * Get all the contentPages with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ContentPage> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" contentPage.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ContentPage> findOne(Long id);

    /**
     * Delete the "id" contentPage.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
