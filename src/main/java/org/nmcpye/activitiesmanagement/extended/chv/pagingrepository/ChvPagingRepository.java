package org.nmcpye.activitiesmanagement.extended.chv.pagingrepository;

import org.nmcpye.activitiesmanagement.domain.chv.Chv;
import org.nmcpye.activitiesmanagement.extended.chv.ChvStore;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Chv entity.
 * created by Hamza Assada 17-11-2021
 */
@SuppressWarnings("unused")
@Repository
public interface ChvPagingRepository
    extends ChvStore, PagingAndSortingRepository<Chv, Long> {
}
