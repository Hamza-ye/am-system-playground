package org.nmcpye.activitiesmanagement.repository;

import java.util.List;
import org.nmcpye.activitiesmanagement.domain.CHVMalariaReportVersion1;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CHVMalariaReportVersion1 entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CHVMalariaReportVersion1Repository extends JpaRepository<CHVMalariaReportVersion1, Long> {
    @Query(
        "select cHVMalariaReportVersion1 from CHVMalariaReportVersion1 cHVMalariaReportVersion1 where cHVMalariaReportVersion1.user.login = ?#{principal.username}"
    )
    List<CHVMalariaReportVersion1> findByUserIsCurrentUser();

    @Query(
        "select cHVMalariaReportVersion1 from CHVMalariaReportVersion1 cHVMalariaReportVersion1 where cHVMalariaReportVersion1.lastUpdatedBy.login = ?#{principal.username}"
    )
    List<CHVMalariaReportVersion1> findByLastUpdatedByIsCurrentUser();
}
