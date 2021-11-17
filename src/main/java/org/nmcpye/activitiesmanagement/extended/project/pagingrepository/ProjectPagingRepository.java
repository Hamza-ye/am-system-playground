package org.nmcpye.activitiesmanagement.extended.project.pagingrepository;

import org.nmcpye.activitiesmanagement.domain.project.Project;
import org.nmcpye.activitiesmanagement.extended.project.ProjectStore;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Project entity.
 * created by Hamza Assada 12-11-2021
 */
@SuppressWarnings("unused")
@Repository
public interface ProjectPagingRepository
    extends ProjectStore, PagingAndSortingRepository<Project, Long> {

}
