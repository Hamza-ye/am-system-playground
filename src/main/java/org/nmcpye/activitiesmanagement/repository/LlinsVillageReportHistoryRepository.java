package org.nmcpye.activitiesmanagement.repository;

import java.util.List;
import org.nmcpye.activitiesmanagement.domain.LlinsVillageReportHistory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the LlinsVillageReportHistory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LlinsVillageReportHistoryRepository extends JpaRepository<LlinsVillageReportHistory, Long> {
    @Query(
        "select lLINSVillageReportHistory from LlinsVillageReportHistory lLINSVillageReportHistory where lLINSVillageReportHistory.createdBy.login = ?#{principal.username}"
    )
    List<LlinsVillageReportHistory> findByUserIsCurrentUser();

    @Query(
        "select lLINSVillageReportHistory from LlinsVillageReportHistory lLINSVillageReportHistory where lLINSVillageReportHistory.lastUpdatedBy.login = ?#{principal.username}"
    )
    List<LlinsVillageReportHistory> findByLastUpdatedByIsCurrentUser();
}
