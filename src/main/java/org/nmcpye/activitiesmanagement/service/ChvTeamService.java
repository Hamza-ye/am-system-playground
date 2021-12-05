package org.nmcpye.activitiesmanagement.service;

import org.nmcpye.activitiesmanagement.domain.ChvTeam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link ChvTeam}.
 */
public interface ChvTeamService {
    /**
     * Save a chvTeam.
     *
     * @param chvTeam the entity to save.
     * @return the persisted entity.
     */
    ChvTeam save(ChvTeam chvTeam);

    /**
     * Partially updates a chvTeam.
     *
     * @param chvTeam the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ChvTeam> partialUpdate(ChvTeam chvTeam);

    /**
     * Get all the chvTeams.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ChvTeam> findAll(Pageable pageable);

    /**
     * Get all the chvTeams with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ChvTeam> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" chvTeam.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ChvTeam> findOne(Long id);

    /**
     * Delete the "id" chvTeam.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
