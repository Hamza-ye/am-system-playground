package org.nmcpye.activitiesmanagement.extended.project;

import org.nmcpye.activitiesmanagement.domain.project.Project;
import org.nmcpye.activitiesmanagement.extended.common.IdentifiableObjectStore;

/**
 * Defines methods for persisting Project.
 * @author Hamza Assada
 *
 */

public interface ProjectStore extends IdentifiableObjectStore<Project> {
    String ID = ProjectStore.class.getName();

    /**
     * Returns number of all people
     * @return number of people
     */
    int getProjectCount();

    /**
     * Returns Project with given projectId.
     *
     * @param projectId UserId
     * @return User with given projectId
     */
    Project getProject(Long projectId);
}
