package org.nmcpye.activitiesmanagement.repository;

import org.nmcpye.activitiesmanagement.domain.LlinsVillageReportHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data SQL repository for the LlinsVillageReportHistory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LlinsVillageReportHistoryRepository extends JpaRepository<LlinsVillageReportHistory, Long> {
    @Query(
        "select llinsVillageReportHistory from LlinsVillageReportHistory llinsVillageReportHistory where llinsVillageReportHistory.createdBy.login = ?#{principal.username}"
    )
    List<LlinsVillageReportHistory> findByCreatedByIsCurrentUser();

    @Query(
        "select llinsVillageReportHistory from LlinsVillageReportHistory llinsVillageReportHistory where llinsVillageReportHistory.lastUpdatedBy.login = ?#{principal.username}"
    )
    List<LlinsVillageReportHistory> findByLastUpdatedByIsCurrentUser();
}
