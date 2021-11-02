package org.nmcpye.activitiesmanagement.service;

import java.util.List;
import java.util.Optional;
import org.nmcpye.activitiesmanagement.domain.Fingerprint;

/**
 * Service Interface for managing {@link Fingerprint}.
 */
public interface FingerprintService {
    /**
     * Save a fingerprint.
     *
     * @param fingerprint the entity to save.
     * @return the persisted entity.
     */
    Fingerprint save(Fingerprint fingerprint);

    /**
     * Partially updates a fingerprint.
     *
     * @param fingerprint the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Fingerprint> partialUpdate(Fingerprint fingerprint);

    /**
     * Get all the fingerprints.
     *
     * @return the list of entities.
     */
    List<Fingerprint> findAll();

    /**
     * Get the "id" fingerprint.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Fingerprint> findOne(Long id);

    /**
     * Delete the "id" fingerprint.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
