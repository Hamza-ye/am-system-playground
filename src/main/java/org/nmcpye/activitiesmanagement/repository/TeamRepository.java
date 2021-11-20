package org.nmcpye.activitiesmanagement.repository;

import java.util.List;
import java.util.Optional;
import org.nmcpye.activitiesmanagement.domain.Team;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Team entity.
 */
@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
    @Query("select team from Team team where team.createdBy.login = ?#{principal.username}")
    List<Team> findByUserIsCurrentUser();

    @Query("select team from Team team where team.lastUpdatedBy.login = ?#{principal.username}")
    List<Team> findByLastUpdatedByIsCurrentUser();

    @Query(
        value = "select distinct team from Team team left join fetch team.assignedToWarehouses",
        countQuery = "select count(distinct team) from Team team"
    )
    Page<Team> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct team from Team team left join fetch team.assignedToWarehouses")
    List<Team> findAllWithEagerRelationships();

    @Query("select team from Team team left join fetch team.assignedToWarehouses where team.id =:id")
    Optional<Team> findOneWithEagerRelationships(@Param("id") Long id);
}
