package org.nmcpye.activitiesmanagement.extended.dataset.pagingrepository;

import org.nmcpye.activitiesmanagement.domain.dataset.DengueCasesReport;
import org.nmcpye.activitiesmanagement.extended.dataset.DengueCasesReportStore;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the DengueCasesReport entity.
 * created by Hamza Assada 17-11-2021
 */
@SuppressWarnings("unused")
@Repository
public interface DengueCasesReportPagingRepository
    extends DengueCasesReportStore, PagingAndSortingRepository<DengueCasesReport, Long> {

}
