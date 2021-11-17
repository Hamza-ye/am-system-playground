package org.nmcpye.activitiesmanagement.repository;

import java.util.List;
import org.nmcpye.activitiesmanagement.domain.demographicdata.DemographicData;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the DemographicData entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DemographicDataRepository extends JpaRepository<DemographicData, Long> {
    @Query("select demographicData from DemographicData demographicData where demographicData.createdBy.login = ?#{principal.username}")
    List<DemographicData> findByUserIsCurrentUser();

    @Query("select demographicData from DemographicData demographicData where demographicData.lastUpdatedBy.login = ?#{principal.username}")
    List<DemographicData> findByLastUpdatedByIsCurrentUser();
}
