package org.nmcpye.activitiesmanagement.repository;

import java.util.List;
import org.nmcpye.activitiesmanagement.domain.LLINSFamilyReportHistory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the LLINSFamilyReportHistory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LLINSFamilyReportHistoryRepository extends JpaRepository<LLINSFamilyReportHistory, Long> {
    @Query(
        "select lLINSFamilyReportHistory from LLINSFamilyReportHistory lLINSFamilyReportHistory where lLINSFamilyReportHistory.user.login = ?#{principal.username}"
    )
    List<LLINSFamilyReportHistory> findByUserIsCurrentUser();

    @Query(
        "select lLINSFamilyReportHistory from LLINSFamilyReportHistory lLINSFamilyReportHistory where lLINSFamilyReportHistory.lastUpdatedBy.login = ?#{principal.username}"
    )
    List<LLINSFamilyReportHistory> findByLastUpdatedByIsCurrentUser();
}
