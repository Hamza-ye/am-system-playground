package org.nmcpye.activitiesmanagement.extended.project;

import org.nmcpye.activitiesmanagement.domain.Project;
import org.nmcpye.activitiesmanagement.domain.organisationunit.OrganisationUnit;
import org.nmcpye.activitiesmanagement.extended.common.IdentifiableObjectStore;
import org.nmcpye.activitiesmanagement.extended.organisationunit.OrganisationUnitHierarchy;
import org.nmcpye.activitiesmanagement.extended.organisationunit.OrganisationUnitQueryParams;

import java.util.Date;
import java.util.List;

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
