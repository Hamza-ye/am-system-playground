package org.nmcpye.activitiesmanagement.extended.dataset.pagingrepository;

import org.nmcpye.activitiesmanagement.domain.dataset.DataSet;
import org.nmcpye.activitiesmanagement.extended.dataset.DataSetStore;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the DataSet entity.
 * created by Hamza Assada 12-11-2021
 */
@SuppressWarnings("unused")
@Repository
public interface DataSetPagingRepository
    extends DataSetStore, PagingAndSortingRepository<DataSet, Long> {

}
