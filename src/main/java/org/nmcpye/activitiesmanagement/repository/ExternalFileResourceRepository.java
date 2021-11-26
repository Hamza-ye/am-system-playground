package org.nmcpye.activitiesmanagement.repository;

import org.nmcpye.activitiesmanagement.domain.fileresource.ExternalFileResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data SQL repository for the ExternalFileResource entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExternalFileResourceRepository extends JpaRepository<ExternalFileResource, Long> {
//    @Query(
//        "select externalFileResource from ExternalFileResource externalFileResource where externalFileResource.createdBy.login = ?#{principal.username}"
//    )
//    List<ExternalFileResource> findByCreatedByIsCurrentUser();

    @Query(
        "select externalFileResource from ExternalFileResource externalFileResource where externalFileResource.lastUpdatedBy.login = ?#{principal.username}"
    )
    List<ExternalFileResource> findByLastUpdatedByIsCurrentUser();
}
