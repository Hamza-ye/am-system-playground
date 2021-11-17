package org.nmcpye.activitiesmanagement.extended.chv.pagingrepository;

import org.nmcpye.activitiesmanagement.domain.chv.CHV;
import org.nmcpye.activitiesmanagement.domain.dataset.DataSet;
import org.nmcpye.activitiesmanagement.extended.chv.ChvStore;
import org.nmcpye.activitiesmanagement.extended.dataset.DataSetStore;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CHV entity.
 * created by Hamza Assada 12-11-2021
 */
@SuppressWarnings("unused")
@Repository
public interface ChvPagingRepository
    extends ChvStore, PagingAndSortingRepository<CHV, Long> {
}
