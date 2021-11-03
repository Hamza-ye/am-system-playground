package org.nmcpye.activitiesmanagement.repository;

import java.util.List;
import java.util.Optional;
import org.nmcpye.activitiesmanagement.domain.person.PeopleGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PeopleGroup entity.
 */
@Repository
public interface PeopleGroupRepository extends JpaRepository<PeopleGroup, Long> {
    @Query("select peopleGroup from PeopleGroup peopleGroup where peopleGroup.user.login = ?#{principal.username}")
    List<PeopleGroup> findByUserIsCurrentUser();

    @Query("select peopleGroup from PeopleGroup peopleGroup where peopleGroup.lastUpdatedBy.login = ?#{principal.username}")
    List<PeopleGroup> findByLastUpdatedByIsCurrentUser();

    @Query(
        value = "select distinct peopleGroup from PeopleGroup peopleGroup left join fetch peopleGroup.members left join fetch peopleGroup.managedByGroups",
        countQuery = "select count(distinct peopleGroup) from PeopleGroup peopleGroup"
    )
    Page<PeopleGroup> findAllWithEagerRelationships(Pageable pageable);

    @Query(
        "select distinct peopleGroup from PeopleGroup peopleGroup left join fetch peopleGroup.members left join fetch peopleGroup.managedByGroups"
    )
    List<PeopleGroup> findAllWithEagerRelationships();

    @Query(
        "select peopleGroup from PeopleGroup peopleGroup left join fetch peopleGroup.members left join fetch peopleGroup.managedByGroups where peopleGroup.id =:id"
    )
    Optional<PeopleGroup> findOneWithEagerRelationships(@Param("id") Long id);
}
