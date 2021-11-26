package org.nmcpye.activitiesmanagement.repository;

import java.util.List;
import org.nmcpye.activitiesmanagement.domain.LlinsFamilyReport;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the LlinsFamilyReport entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LlinsFamilyReportRepository extends JpaRepository<LlinsFamilyReport, Long> {
    @Query("select lLINSFamilyReport from LlinsFamilyReport lLINSFamilyReport where lLINSFamilyReport.createdBy.login = ?#{principal.username}")
    List<LlinsFamilyReport> findByUserIsCurrentUser();

    @Query(
        "select lLINSFamilyReport from LlinsFamilyReport lLINSFamilyReport where lLINSFamilyReport.lastUpdatedBy.login = ?#{principal.username}"
    )
    List<LlinsFamilyReport> findByLastUpdatedByIsCurrentUser();
}
