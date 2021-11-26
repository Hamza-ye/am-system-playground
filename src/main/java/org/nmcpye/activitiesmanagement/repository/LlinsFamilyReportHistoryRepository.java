package org.nmcpye.activitiesmanagement.repository;

import org.nmcpye.activitiesmanagement.domain.LlinsFamilyReportHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data SQL repository for the LlinsFamilyReportHistory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LlinsFamilyReportHistoryRepository extends JpaRepository<LlinsFamilyReportHistory, Long> {
    @Query(
        "select llinsFamilyReportHistory from LlinsFamilyReportHistory llinsFamilyReportHistory where llinsFamilyReportHistory.createdBy.login = ?#{principal.username}"
    )
    List<LlinsFamilyReportHistory> findByCreatedByIsCurrentUser();

    @Query(
        "select llinsFamilyReportHistory from LlinsFamilyReportHistory llinsFamilyReportHistory where llinsFamilyReportHistory.lastUpdatedBy.login = ?#{principal.username}"
    )
    List<LlinsFamilyReportHistory> findByLastUpdatedByIsCurrentUser();
}
