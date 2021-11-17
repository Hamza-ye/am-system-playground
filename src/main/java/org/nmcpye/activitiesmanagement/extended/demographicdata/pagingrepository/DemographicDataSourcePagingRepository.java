package org.nmcpye.activitiesmanagement.extended.demographicdata.pagingrepository;

import org.nmcpye.activitiesmanagement.domain.demographicdata.DemographicDataSource;
import org.nmcpye.activitiesmanagement.extended.demographicdata.DemographicDataSourceStore;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the DemographicDataSource entity.
 * created by Hamza Assada 12-11-2021
 */
@SuppressWarnings("unused")
@Repository
public interface DemographicDataSourcePagingRepository
    extends DemographicDataSourceStore, PagingAndSortingRepository<DemographicDataSource, Long> {

}
