package org.nmcpye.activitiesmanagement.extended.demographicdata.pagingrepository;

import org.nmcpye.activitiesmanagement.domain.demographicdata.DemographicData;
import org.nmcpye.activitiesmanagement.extended.demographicdata.DemographicDataStore;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the DemographicData entity.
 * created by Hamza Assada 12-11-2021
 */
@SuppressWarnings("unused")
@Repository
public interface DemographicDataPagingRepository
    extends DemographicDataStore, PagingAndSortingRepository<DemographicData, Long> {

}
