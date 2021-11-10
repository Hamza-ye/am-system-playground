package org.nmcpye.activitiesmanagement.repository;

import java.util.List;
import org.nmcpye.activitiesmanagement.domain.Project;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Project entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    @Query("select project from Project project where project.createdBy.login = ?#{principal.username}")
    List<Project> findByUserIsCurrentUser();

    @Query("select project from Project project where project.lastUpdatedBy.login = ?#{principal.username}")
    List<Project> findByLastUpdatedByIsCurrentUser();
}
