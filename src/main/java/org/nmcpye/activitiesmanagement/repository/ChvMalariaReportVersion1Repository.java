package org.nmcpye.activitiesmanagement.repository;

import org.nmcpye.activitiesmanagement.domain.ChvMalariaReportVersion1;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data SQL repository for the ChvMalariaReportVersion1 entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ChvMalariaReportVersion1Repository extends JpaRepository<ChvMalariaReportVersion1, Long> {
    @Query(
        "select chvMalariaReportVersion1 from ChvMalariaReportVersion1 chvMalariaReportVersion1 where chvMalariaReportVersion1.createdBy.login = ?#{principal.username}"
    )
    List<ChvMalariaReportVersion1> findByCreatedByIsCurrentUser();

    @Query(
        "select chvMalariaReportVersion1 from ChvMalariaReportVersion1 chvMalariaReportVersion1 where chvMalariaReportVersion1.lastUpdatedBy.login = ?#{principal.username}"
    )
    List<ChvMalariaReportVersion1> findByLastUpdatedByIsCurrentUser();
}
