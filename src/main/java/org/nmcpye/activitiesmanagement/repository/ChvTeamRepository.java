package org.nmcpye.activitiesmanagement.repository;

import org.nmcpye.activitiesmanagement.domain.ChvTeam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data SQL repository for the ChvTeam entity.
 */
@Repository
public interface ChvTeamRepository extends JpaRepository<ChvTeam, Long> {
    @Query("select chvTeam from ChvTeam chvTeam where chvTeam.createdBy.login = ?#{principal.username}")
    List<ChvTeam> findByCreatedByIsCurrentUser();

    @Query("select chvTeam from ChvTeam chvTeam where chvTeam.lastUpdatedBy.login = ?#{principal.username}")
    List<ChvTeam> findByLastUpdatedByIsCurrentUser();

    @Query(
        value = "select distinct chvTeam from ChvTeam chvTeam left join fetch chvTeam.responsibleForChvs",
        countQuery = "select count(distinct chvTeam) from ChvTeam chvTeam"
    )
    Page<ChvTeam> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct chvTeam from ChvTeam chvTeam left join fetch chvTeam.responsibleForChvs")
    List<ChvTeam> findAllWithEagerRelationships();

    @Query("select chvTeam from ChvTeam chvTeam left join fetch chvTeam.responsibleForChvs where chvTeam.id =:id")
    Optional<ChvTeam> findOneWithEagerRelationships(@Param("id") Long id);
}
