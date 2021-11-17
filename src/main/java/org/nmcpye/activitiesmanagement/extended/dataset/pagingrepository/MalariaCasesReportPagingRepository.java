package org.nmcpye.activitiesmanagement.extended.dataset.pagingrepository;

import org.nmcpye.activitiesmanagement.domain.dataset.MalariaCasesReport;
import org.nmcpye.activitiesmanagement.extended.dataset.MalariaCasesReportStore;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the MalariaCasesReport entity.
 * created by Hamza Assada 17-11-2021
 */
@SuppressWarnings("unused")
@Repository
public interface MalariaCasesReportPagingRepository
    extends MalariaCasesReportStore, PagingAndSortingRepository<MalariaCasesReport, Long> {

}
