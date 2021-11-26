package org.nmcpye.activitiesmanagement.repository;

import java.util.List;
import org.nmcpye.activitiesmanagement.domain.LlinsVillageReport;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the LlinsVillageReport entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LlinsVillageReportRepository extends JpaRepository<LlinsVillageReport, Long> {
    @Query(
        "select lLINSVillageReport from LlinsVillageReport lLINSVillageReport where lLINSVillageReport.createdBy.login = ?#{principal.username}"
    )
    List<LlinsVillageReport> findByUserIsCurrentUser();

    @Query(
        "select lLINSVillageReport from LlinsVillageReport lLINSVillageReport where lLINSVillageReport.lastUpdatedBy.login = ?#{principal.username}"
    )
    List<LlinsVillageReport> findByLastUpdatedByIsCurrentUser();
}
