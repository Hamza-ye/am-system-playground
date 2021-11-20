package org.nmcpye.activitiesmanagement.repository;

import java.util.List;
import org.nmcpye.activitiesmanagement.domain.LLINSVillageReport;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the LLINSVillageReport entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LLINSVillageReportRepository extends JpaRepository<LLINSVillageReport, Long> {
    @Query(
        "select lLINSVillageReport from LLINSVillageReport lLINSVillageReport where lLINSVillageReport.createdBy.login = ?#{principal.username}"
    )
    List<LLINSVillageReport> findByUserIsCurrentUser();

    @Query(
        "select lLINSVillageReport from LLINSVillageReport lLINSVillageReport where lLINSVillageReport.lastUpdatedBy.login = ?#{principal.username}"
    )
    List<LLINSVillageReport> findByLastUpdatedByIsCurrentUser();
}
