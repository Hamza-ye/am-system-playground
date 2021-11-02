package org.nmcpye.activitiesmanagement.repository;

import java.util.List;
import org.nmcpye.activitiesmanagement.domain.LLINSVillageReportHistory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the LLINSVillageReportHistory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LLINSVillageReportHistoryRepository extends JpaRepository<LLINSVillageReportHistory, Long> {
    @Query(
        "select lLINSVillageReportHistory from LLINSVillageReportHistory lLINSVillageReportHistory where lLINSVillageReportHistory.user.login = ?#{principal.username}"
    )
    List<LLINSVillageReportHistory> findByUserIsCurrentUser();

    @Query(
        "select lLINSVillageReportHistory from LLINSVillageReportHistory lLINSVillageReportHistory where lLINSVillageReportHistory.lastUpdatedBy.login = ?#{principal.username}"
    )
    List<LLINSVillageReportHistory> findByLastUpdatedByIsCurrentUser();
}
