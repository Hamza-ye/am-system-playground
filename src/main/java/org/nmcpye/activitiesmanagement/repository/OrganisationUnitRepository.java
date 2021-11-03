package org.nmcpye.activitiesmanagement.repository;

import java.util.List;
import org.nmcpye.activitiesmanagement.domain.organisationunit.OrganisationUnit;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the OrganisationUnit entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrganisationUnitRepository extends JpaRepository<OrganisationUnit, Long> {
    @Query("select organisationUnit from OrganisationUnit organisationUnit where organisationUnit.user.login = ?#{principal.username}")
    List<OrganisationUnit> findByUserIsCurrentUser();

    @Query(
        "select organisationUnit from OrganisationUnit organisationUnit where organisationUnit.lastUpdatedBy.login = ?#{principal.username}"
    )
    List<OrganisationUnit> findByLastUpdatedByIsCurrentUser();
}
