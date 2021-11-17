package org.nmcpye.activitiesmanagement.extended.project;

import org.nmcpye.activitiesmanagement.domain.project.Project;
import org.nmcpye.activitiesmanagement.extended.common.IdentifiableObjectStore;

/**
 * Defines methods for persisting Project.
 *
 * @author Hamza Assada on 17/11/2021.
 */
public interface ProjectStore extends IdentifiableObjectStore<Project> {
    String ID = ProjectStore.class.getName();

    /**
     * Returns number of all Projects
     *
     * @return number of Projects
     */
    int getProjectCount();

    /**
     * Returns Project with given projectId.
     *
     * @param projectId ProjectId
     * @return Project with given projectId
     */
    Project getProject(Long projectId);
}
