package org.nmcpye.activitiesmanagement.service;

import java.util.List;
import java.util.Optional;
import org.nmcpye.activitiesmanagement.domain.CHVTeam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link CHVTeam}.
 */
public interface CHVTeamService {
    /**
     * Save a cHVTeam.
     *
     * @param cHVTeam the entity to save.
     * @return the persisted entity.
     */
    CHVTeam save(CHVTeam cHVTeam);

    /**
     * Partially updates a cHVTeam.
     *
     * @param cHVTeam the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CHVTeam> partialUpdate(CHVTeam cHVTeam);

    /**
     * Get all the cHVTeams.
     *
     * @return the list of entities.
     */
    List<CHVTeam> findAll();

    /**
     * Get all the cHVTeams with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CHVTeam> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" cHVTeam.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CHVTeam> findOne(Long id);

    /**
     * Delete the "id" cHVTeam.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
