package org.nmcpye.activitiesmanagement.repository;

import java.util.List;
import org.nmcpye.activitiesmanagement.domain.LLINSFamilyReport;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the LLINSFamilyReport entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LLINSFamilyReportRepository extends JpaRepository<LLINSFamilyReport, Long> {
    @Query("select lLINSFamilyReport from LLINSFamilyReport lLINSFamilyReport where lLINSFamilyReport.user.login = ?#{principal.username}")
    List<LLINSFamilyReport> findByUserIsCurrentUser();

    @Query(
        "select lLINSFamilyReport from LLINSFamilyReport lLINSFamilyReport where lLINSFamilyReport.lastUpdatedBy.login = ?#{principal.username}"
    )
    List<LLINSFamilyReport> findByLastUpdatedByIsCurrentUser();
}
