package org.nmcpye.activitiesmanagement.service;

import org.nmcpye.activitiesmanagement.domain.ContentPage;

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
