package org.nmcpye.activitiesmanagement.repository;

import java.util.List;
import org.nmcpye.activitiesmanagement.domain.CHVMalariaCaseReport;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CHVMalariaCaseReport entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CHVMalariaCaseReportRepository extends JpaRepository<CHVMalariaCaseReport, Long> {
    @Query(
        "select cHVMalariaCaseReport from CHVMalariaCaseReport cHVMalariaCaseReport where cHVMalariaCaseReport.createdBy.login = ?#{principal.username}"
    )
    List<CHVMalariaCaseReport> findByUserIsCurrentUser();

    @Query(
        "select cHVMalariaCaseReport from CHVMalariaCaseReport cHVMalariaCaseReport where cHVMalariaCaseReport.lastUpdatedBy.login = ?#{principal.username}"
    )
    List<CHVMalariaCaseReport> findByLastUpdatedByIsCurrentUser();
}
