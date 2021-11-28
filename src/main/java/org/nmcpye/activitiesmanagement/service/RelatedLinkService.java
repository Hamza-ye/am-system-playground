package org.nmcpye.activitiesmanagement.service;

import org.nmcpye.activitiesmanagement.domain.RelatedLink;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link RelatedLink}.
 */
public interface RelatedLinkService {
    /**
     * Save a relatedLink.
     *
     * @param relatedLink the entity to save.
     * @return the persisted entity.
     */
    RelatedLink save(RelatedLink relatedLink);

    /**
     * Partially updates a relatedLink.
     *
     * @param relatedLink the entity to update partially.
     * @return the persisted entity.
     */
    Optional<RelatedLink> partialUpdate(RelatedLink relatedLink);

    /**
     * Get all the relatedLinks.
     *
     * @return the list of entities.
     */
    List<RelatedLink> findAll();

    /**
     * Get the "id" relatedLink.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<RelatedLink> findOne(Long id);

    /**
     * Delete the "id" relatedLink.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
