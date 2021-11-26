package org.nmcpye.activitiesmanagement.repository;

import java.util.List;
import org.nmcpye.activitiesmanagement.domain.ChvMalariaReportVersion1;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ChvMalariaReportVersion1 entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ChvMalariaReportVersion1Repository extends JpaRepository<ChvMalariaReportVersion1, Long> {
    @Query(
        "select cHVMalariaReportVersion1 from ChvMalariaReportVersion1 cHVMalariaReportVersion1 where cHVMalariaReportVersion1.createdBy.login = ?#{principal.username}"
    )
    List<ChvMalariaReportVersion1> findByUserIsCurrentUser();

    @Query(
        "select cHVMalariaReportVersion1 from ChvMalariaReportVersion1 cHVMalariaReportVersion1 where cHVMalariaReportVersion1.lastUpdatedBy.login = ?#{principal.username}"
    )
    List<ChvMalariaReportVersion1> findByLastUpdatedByIsCurrentUser();
}
