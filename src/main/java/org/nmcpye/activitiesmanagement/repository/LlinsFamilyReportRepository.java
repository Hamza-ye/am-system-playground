package org.nmcpye.activitiesmanagement.repository;

import org.nmcpye.activitiesmanagement.domain.LlinsFamilyReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data SQL repository for the LlinsFamilyReport entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LlinsFamilyReportRepository extends JpaRepository<LlinsFamilyReport, Long> {
    @Query(
        "select llinsFamilyReport from LlinsFamilyReport llinsFamilyReport where llinsFamilyReport.createdBy.login = ?#{principal.username}"
    )
    List<LlinsFamilyReport> findByCreatedByIsCurrentUser();

    @Query(
        "select llinsFamilyReport from LlinsFamilyReport llinsFamilyReport where llinsFamilyReport.lastUpdatedBy.login = ?#{principal.username}"
    )
    List<LlinsFamilyReport> findByLastUpdatedByIsCurrentUser();
}
