package org.nmcpye.activitiesmanagement.repository;

import java.util.List;
import org.nmcpye.activitiesmanagement.domain.demographicdata.DemographicDataSource;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the DemographicDataSource entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DemographicDataSourceRepository extends JpaRepository<DemographicDataSource, Long> {
    @Query(
        "select demographicDataSource from DemographicDataSource demographicDataSource where demographicDataSource.createdBy.login = ?#{principal.username}"
    )
    List<DemographicDataSource> findByUserIsCurrentUser();

    @Query(
        "select demographicDataSource from DemographicDataSource demographicDataSource where demographicDataSource.lastUpdatedBy.login = ?#{principal.username}"
    )
    List<DemographicDataSource> findByLastUpdatedByIsCurrentUser();
}
