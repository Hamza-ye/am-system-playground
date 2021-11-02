package org.nmcpye.activitiesmanagement.service;

import java.util.List;
import java.util.Optional;
import org.nmcpye.activitiesmanagement.domain.StatusOfCoverage;

/**
 * Service Interface for managing {@link StatusOfCoverage}.
 */
public interface StatusOfCoverageService {
    /**
     * Save a statusOfCoverage.
     *
     * @param statusOfCoverage the entity to save.
     * @return the persisted entity.
     */
    StatusOfCoverage save(StatusOfCoverage statusOfCoverage);

    /**
     * Partially updates a statusOfCoverage.
     *
     * @param statusOfCoverage the entity to update partially.
     * @return the persisted entity.
     */
    Optional<StatusOfCoverage> partialUpdate(StatusOfCoverage statusOfCoverage);

    /**
     * Get all the statusOfCoverages.
     *
     * @return the list of entities.
     */
    List<StatusOfCoverage> findAll();

    /**
     * Get the "id" statusOfCoverage.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<StatusOfCoverage> findOne(Long id);

    /**
     * Delete the "id" statusOfCoverage.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
