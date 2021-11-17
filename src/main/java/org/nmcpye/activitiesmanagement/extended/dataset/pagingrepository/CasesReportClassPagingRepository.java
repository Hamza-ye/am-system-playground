package org.nmcpye.activitiesmanagement.extended.dataset.pagingrepository;

import org.nmcpye.activitiesmanagement.domain.dataset.CasesReportClass;
import org.nmcpye.activitiesmanagement.extended.dataset.CasesReportClassStore;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CasesReportClass entity.
 * created by Hamza Assada 12-11-2021
 */
@SuppressWarnings("unused")
@Repository
public interface CasesReportClassPagingRepository
    extends CasesReportClassStore, PagingAndSortingRepository<CasesReportClass, Long> {

}
