package org.nmcpye.activitiesmanagement.repository;

import java.util.List;
import org.nmcpye.activitiesmanagement.domain.ChvMalariaCaseReport;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ChvMalariaCaseReport entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ChvMalariaCaseReportRepository extends JpaRepository<ChvMalariaCaseReport, Long> {
    @Query(
        "select cHVMalariaCaseReport from ChvMalariaCaseReport cHVMalariaCaseReport where cHVMalariaCaseReport.createdBy.login = ?#{principal.username}"
    )
    List<ChvMalariaCaseReport> findByUserIsCurrentUser();

    @Query(
        "select cHVMalariaCaseReport from ChvMalariaCaseReport cHVMalariaCaseReport where cHVMalariaCaseReport.lastUpdatedBy.login = ?#{principal.username}"
    )
    List<ChvMalariaCaseReport> findByLastUpdatedByIsCurrentUser();
}
