package org.nmcpye.activitiesmanagement.repository;

import java.util.List;
import org.nmcpye.activitiesmanagement.domain.StatusOfCoverage;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the StatusOfCoverage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StatusOfCoverageRepository extends JpaRepository<StatusOfCoverage, Long> {
    @Query("select statusOfCoverage from StatusOfCoverage statusOfCoverage where statusOfCoverage.user.login = ?#{principal.username}")
    List<StatusOfCoverage> findByUserIsCurrentUser();

    @Query(
        "select statusOfCoverage from StatusOfCoverage statusOfCoverage where statusOfCoverage.lastUpdatedBy.login = ?#{principal.username}"
    )
    List<StatusOfCoverage> findByLastUpdatedByIsCurrentUser();
}
