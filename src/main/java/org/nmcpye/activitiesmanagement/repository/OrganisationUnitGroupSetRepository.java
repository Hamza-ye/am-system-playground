package org.nmcpye.activitiesmanagement.repository;

import java.util.List;
import java.util.Optional;
import org.nmcpye.activitiesmanagement.domain.organisationunit.OrganisationUnitGroupSet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the OrganisationUnitGroupSet entity.
 */
@Repository
public interface OrganisationUnitGroupSetRepository extends JpaRepository<OrganisationUnitGroupSet, Long> {
    @Query(
        "select organisationUnitGroupSet from OrganisationUnitGroupSet organisationUnitGroupSet where organisationUnitGroupSet.user.login = ?#{principal.username}"
    )
    List<OrganisationUnitGroupSet> findByUserIsCurrentUser();

    @Query(
        "select organisationUnitGroupSet from OrganisationUnitGroupSet organisationUnitGroupSet where organisationUnitGroupSet.lastUpdatedBy.login = ?#{principal.username}"
    )
    List<OrganisationUnitGroupSet> findByLastUpdatedByIsCurrentUser();

    @Query(
        value = "select distinct organisationUnitGroupSet from OrganisationUnitGroupSet organisationUnitGroupSet left join fetch organisationUnitGroupSet.organisationUnitGroups",
        countQuery = "select count(distinct organisationUnitGroupSet) from OrganisationUnitGroupSet organisationUnitGroupSet"
    )
    Page<OrganisationUnitGroupSet> findAllWithEagerRelationships(Pageable pageable);

    @Query(
        "select distinct organisationUnitGroupSet from OrganisationUnitGroupSet organisationUnitGroupSet left join fetch organisationUnitGroupSet.organisationUnitGroups"
    )
    List<OrganisationUnitGroupSet> findAllWithEagerRelationships();

    @Query(
        "select organisationUnitGroupSet from OrganisationUnitGroupSet organisationUnitGroupSet left join fetch organisationUnitGroupSet.organisationUnitGroups where organisationUnitGroupSet.id =:id"
    )
    Optional<OrganisationUnitGroupSet> findOneWithEagerRelationships(@Param("id") Long id);
}
