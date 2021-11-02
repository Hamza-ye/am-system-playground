package org.nmcpye.activitiesmanagement.repository;

import java.util.List;
import org.nmcpye.activitiesmanagement.domain.DengueCasesReport;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the DengueCasesReport entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DengueCasesReportRepository extends JpaRepository<DengueCasesReport, Long> {
    @Query("select dengueCasesReport from DengueCasesReport dengueCasesReport where dengueCasesReport.user.login = ?#{principal.username}")
    List<DengueCasesReport> findByUserIsCurrentUser();

    @Query(
        "select dengueCasesReport from DengueCasesReport dengueCasesReport where dengueCasesReport.lastUpdatedBy.login = ?#{principal.username}"
    )
    List<DengueCasesReport> findByLastUpdatedByIsCurrentUser();
}
