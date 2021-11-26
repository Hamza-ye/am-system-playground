package org.nmcpye.activitiesmanagement.repository;

import org.nmcpye.activitiesmanagement.domain.fileresource.FileResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data SQL repository for the FileResource entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FileResourceRepository extends JpaRepository<FileResource, Long> {
    @Query("select fileResource from FileResource fileResource where fileResource.createdBy.login = ?#{principal.username}")
    List<FileResource> findByCreatedByIsCurrentUser();

    @Query("select fileResource from FileResource fileResource where fileResource.lastUpdatedBy.login = ?#{principal.username}")
    List<FileResource> findByLastUpdatedByIsCurrentUser();
}
