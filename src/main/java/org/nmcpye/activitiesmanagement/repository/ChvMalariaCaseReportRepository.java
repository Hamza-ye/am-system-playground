package org.nmcpye.activitiesmanagement.repository;

import org.nmcpye.activitiesmanagement.domain.ChvMalariaCaseReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data SQL repository for the ChvMalariaCaseReport entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ChvMalariaCaseReportRepository extends JpaRepository<ChvMalariaCaseReport, Long> {
    @Query(
        "select chvMalariaCaseReport from ChvMalariaCaseReport chvMalariaCaseReport where chvMalariaCaseReport.createdBy.login = ?#{principal.username}"
    )
    List<ChvMalariaCaseReport> findByCreatedByIsCurrentUser();

    @Query(
        "select chvMalariaCaseReport from ChvMalariaCaseReport chvMalariaCaseReport where chvMalariaCaseReport.lastUpdatedBy.login = ?#{principal.username}"
    )
    List<ChvMalariaCaseReport> findByLastUpdatedByIsCurrentUser();
}
