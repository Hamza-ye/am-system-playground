package org.nmcpye.activitiesmanagement.extended.organisationunit.pagingrepository;

import org.nmcpye.activitiesmanagement.domain.organisationunit.OrganisationUnitGroupSet;
import org.nmcpye.activitiesmanagement.extended.organisationunit.OrganisationUnitGroupSetStore;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data SQL repository for the OrganisationUnitGroupSet entity.
 * created by Hamza Assada 12-11-2021
 */
@Repository
public interface OrganisationUnitGroupSetPagingRepository
    extends OrganisationUnitGroupSetStore, PagingAndSortingRepository<OrganisationUnitGroupSet, Long> {
}
