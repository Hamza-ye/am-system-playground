package org.nmcpye.activitiesmanagement.extended.organisationunit.pagingrepository;

import org.nmcpye.activitiesmanagement.domain.organisationunit.OrganisationUnit;
import org.nmcpye.activitiesmanagement.extended.organisationunit.OrganisationUnitStore;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the OrganisationUnit entity.
 * created by Hamza Assada 12-11-2021
 */
@SuppressWarnings("unused")
@Repository
public interface OrganisationUnitPagingRepository
    extends OrganisationUnitStore, PagingAndSortingRepository<OrganisationUnit, Long> {

}
