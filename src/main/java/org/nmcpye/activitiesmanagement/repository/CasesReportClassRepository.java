package org.nmcpye.activitiesmanagement.repository;

import java.util.List;
import org.nmcpye.activitiesmanagement.domain.CasesReportClass;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CasesReportClass entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CasesReportClassRepository extends JpaRepository<CasesReportClass, Long> {
    @Query("select casesReportClass from CasesReportClass casesReportClass where casesReportClass.user.login = ?#{principal.username}")
    List<CasesReportClass> findByUserIsCurrentUser();

    @Query(
        "select casesReportClass from CasesReportClass casesReportClass where casesReportClass.lastUpdatedBy.login = ?#{principal.username}"
    )
    List<CasesReportClass> findByLastUpdatedByIsCurrentUser();
}
