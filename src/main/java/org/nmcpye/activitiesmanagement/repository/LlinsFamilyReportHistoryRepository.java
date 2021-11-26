package org.nmcpye.activitiesmanagement.repository;

import java.util.List;
import org.nmcpye.activitiesmanagement.domain.LlinsFamilyReportHistory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the LlinsFamilyReportHistory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LlinsFamilyReportHistoryRepository extends JpaRepository<LlinsFamilyReportHistory, Long> {
    @Query(
        "select lLINSFamilyReportHistory from LlinsFamilyReportHistory lLINSFamilyReportHistory where lLINSFamilyReportHistory.createdBy.login = ?#{principal.username}"
    )
    List<LlinsFamilyReportHistory> findByUserIsCurrentUser();

    @Query(
        "select lLINSFamilyReportHistory from LlinsFamilyReportHistory lLINSFamilyReportHistory where lLINSFamilyReportHistory.lastUpdatedBy.login = ?#{principal.username}"
    )
    List<LlinsFamilyReportHistory> findByLastUpdatedByIsCurrentUser();
}
