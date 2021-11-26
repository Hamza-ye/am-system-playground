package org.nmcpye.activitiesmanagement.service;

import java.util.List;
import java.util.Optional;
import org.nmcpye.activitiesmanagement.domain.ChvTeam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link ChvTeam}.
 */
public interface ChvTeamService {
    /**
     * Save a cHVTeam.
     *
     * @param cHVTeam the entity to save.
     * @return the persisted entity.
     */
    ChvTeam save(ChvTeam cHVTeam);

    /**
     * Partially updates a cHVTeam.
     *
     * @param cHVTeam the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ChvTeam> partialUpdate(ChvTeam cHVTeam);

    /**
     * Get all the cHVTeams.
     *
     * @return the list of entities.
     */
    List<ChvTeam> findAll();

    /**
     * Get all the cHVTeams with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ChvTeam> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" cHVTeam.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ChvTeam> findOne(Long id);

    /**
     * Delete the "id" cHVTeam.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
