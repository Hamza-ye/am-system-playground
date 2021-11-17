package org.nmcpye.activitiesmanagement.repository;

import java.util.List;
import org.nmcpye.activitiesmanagement.domain.dataset.MalariaCasesReport;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the MalariaCasesReport entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MalariaCasesReportRepository extends JpaRepository<MalariaCasesReport, Long> {
    @Query(
        "select malariaCasesReport from MalariaCasesReport malariaCasesReport where malariaCasesReport.createdBy.login = ?#{principal.username}"
    )
    List<MalariaCasesReport> findByUserIsCurrentUser();

    @Query(
        "select malariaCasesReport from MalariaCasesReport malariaCasesReport where malariaCasesReport.lastUpdatedBy.login = ?#{principal.username}"
    )
    List<MalariaCasesReport> findByLastUpdatedByIsCurrentUser();
}
