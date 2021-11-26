package org.nmcpye.activitiesmanagement.repository;

import org.nmcpye.activitiesmanagement.domain.LlinsVillageReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data SQL repository for the LlinsVillageReport entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LlinsVillageReportRepository extends JpaRepository<LlinsVillageReport, Long> {
    @Query(
        "select llinsVillageReport from LlinsVillageReport llinsVillageReport where llinsVillageReport.createdBy.login = ?#{principal.username}"
    )
    List<LlinsVillageReport> findByCreatedByIsCurrentUser();

    @Query(
        "select llinsVillageReport from LlinsVillageReport llinsVillageReport where llinsVillageReport.lastUpdatedBy.login = ?#{principal.username}"
    )
    List<LlinsVillageReport> findByLastUpdatedByIsCurrentUser();
}
