package org.nmcpye.activitiesmanagement.repository;

import java.util.List;
import java.util.Optional;
import org.nmcpye.activitiesmanagement.domain.CHVTeam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CHVTeam entity.
 */
@Repository
public interface CHVTeamRepository extends JpaRepository<CHVTeam, Long> {
    @Query("select cHVTeam from CHVTeam cHVTeam where cHVTeam.user.login = ?#{principal.username}")
    List<CHVTeam> findByUserIsCurrentUser();

    @Query("select cHVTeam from CHVTeam cHVTeam where cHVTeam.lastUpdatedBy.login = ?#{principal.username}")
    List<CHVTeam> findByLastUpdatedByIsCurrentUser();

    @Query(
        value = "select distinct cHVTeam from CHVTeam cHVTeam left join fetch cHVTeam.responsibleForChvs",
        countQuery = "select count(distinct cHVTeam) from CHVTeam cHVTeam"
    )
    Page<CHVTeam> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct cHVTeam from CHVTeam cHVTeam left join fetch cHVTeam.responsibleForChvs")
    List<CHVTeam> findAllWithEagerRelationships();

    @Query("select cHVTeam from CHVTeam cHVTeam left join fetch cHVTeam.responsibleForChvs where cHVTeam.id =:id")
    Optional<CHVTeam> findOneWithEagerRelationships(@Param("id") Long id);
}
