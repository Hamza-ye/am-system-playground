package org.nmcpye.activitiesmanagement.repository;

import java.util.List;
import java.util.Optional;
import org.nmcpye.activitiesmanagement.domain.OrganisationUnitGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the OrganisationUnitGroup entity.
 */
@Repository
public interface OrganisationUnitGroupRepository extends JpaRepository<OrganisationUnitGroup, Long> {
    @Query(
        "select organisationUnitGroup from OrganisationUnitGroup organisationUnitGroup where organisationUnitGroup.user.login = ?#{principal.username}"
    )
    List<OrganisationUnitGroup> findByUserIsCurrentUser();

    @Query(
        "select organisationUnitGroup from OrganisationUnitGroup organisationUnitGroup where organisationUnitGroup.lastUpdatedBy.login = ?#{principal.username}"
    )
    List<OrganisationUnitGroup> findByLastUpdatedByIsCurrentUser();

    @Query(
        value = "select distinct organisationUnitGroup from OrganisationUnitGroup organisationUnitGroup left join fetch organisationUnitGroup.members",
        countQuery = "select count(distinct organisationUnitGroup) from OrganisationUnitGroup organisationUnitGroup"
    )
    Page<OrganisationUnitGroup> findAllWithEagerRelationships(Pageable pageable);

    @Query(
        "select distinct organisationUnitGroup from OrganisationUnitGroup organisationUnitGroup left join fetch organisationUnitGroup.members"
    )
    List<OrganisationUnitGroup> findAllWithEagerRelationships();

    @Query(
        "select organisationUnitGroup from OrganisationUnitGroup organisationUnitGroup left join fetch organisationUnitGroup.members where organisationUnitGroup.id =:id"
    )
    Optional<OrganisationUnitGroup> findOneWithEagerRelationships(@Param("id") Long id);
}
