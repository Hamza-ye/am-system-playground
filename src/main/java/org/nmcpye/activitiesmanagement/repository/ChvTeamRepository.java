package org.nmcpye.activitiesmanagement.repository;

import java.util.List;
import java.util.Optional;
import org.nmcpye.activitiesmanagement.domain.ChvTeam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ChvTeam entity.
 */
@Repository
public interface ChvTeamRepository extends JpaRepository<ChvTeam, Long> {
    @Query("select cHVTeam from ChvTeam cHVTeam where cHVTeam.createdBy.login = ?#{principal.username}")
    List<ChvTeam> findByUserIsCurrentUser();

    @Query("select cHVTeam from ChvTeam cHVTeam where cHVTeam.lastUpdatedBy.login = ?#{principal.username}")
    List<ChvTeam> findByLastUpdatedByIsCurrentUser();

    @Query(
        value = "select distinct cHVTeam from ChvTeam cHVTeam left join fetch cHVTeam.responsibleForChvs",
        countQuery = "select count(distinct cHVTeam) from ChvTeam cHVTeam"
    )
    Page<ChvTeam> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct cHVTeam from ChvTeam cHVTeam left join fetch cHVTeam.responsibleForChvs")
    List<ChvTeam> findAllWithEagerRelationships();

    @Query("select cHVTeam from ChvTeam cHVTeam left join fetch cHVTeam.responsibleForChvs where cHVTeam.id =:id")
    Optional<ChvTeam> findOneWithEagerRelationships(@Param("id") Long id);
}
