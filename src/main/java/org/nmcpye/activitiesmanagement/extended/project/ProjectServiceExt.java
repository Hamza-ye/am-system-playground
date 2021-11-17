package org.nmcpye.activitiesmanagement.extended.project;

import org.nmcpye.activitiesmanagement.domain.User;
import org.nmcpye.activitiesmanagement.domain.project.Project;

import java.util.List;

/**
 * Created by Hamza on 17/11/2021.
 */
public interface ProjectServiceExt {
    String ID = ProjectServiceExt.class.getName();

    // -------------------------------------------------------------------------
    // Project
    // -------------------------------------------------------------------------

    /**
     * Adds a Project.
     *
     * @param project The Project to add.
     * @return The generated unique identifier for this Project.
     */
    Long addProject(Project project);

    /**
     * Updates a Project.
     *
     * @param project The Project to update.
     */
    void updateProject(Project project);

    /**
     * Deletes a Project.
     *
     * @param project The Project to delete.
     */
    void deleteProject(Project project);

    /**
     * Get a Project
     *
     * @param id The unique identifier for the Project to get.
     * @return The Project with the given id or null if it does not exist.
     */
    Project getProject(Long id);

    /**
     * Returns the Project with the given UID.
     *
     * @param uid the UID.
     * @return the Project with the given UID, or null if no match.
     */
    Project getProject(String uid);

    /**
     * Returns the Project with the given UID. Bypasses the ACL system.
     *
     * @param uid the UID.
     * @return the Project with the given UID, or null if no match.
     */
    Project getProjectNoAcl(String uid);

    /**
     * Get all Projects.
     *
     * @return A list containing all Projects.
     */
    List<Project> getAllProjects();

    /**
     * Returns the data sets which current user have READ access. If the current
     * user has the ALL authority then all data sets are returned.
     */
    List<Project> getAllDataRead();

    /**
     * Returns the data sets which given user have READ access. If the current
     * user has the ALL authority then all data sets are returned.
     *
     * @param user the user to query for data set list.
     * @return a list of data sets which the given user has data read access to.
     */
    List<Project> getUserDataRead(User user);

    /**
     * Returns the data sets which current user have WRITE access. If the
     * current user has the ALL authority then all data sets are returned.
     */
    List<Project> getAllDataWrite();

    /**
     * Returns the data sets which current user have WRITE access. If the
     * current user has the ALL authority then all data sets are returned.
     *
     * @param user the user to query for data set list.
     * @return a list of data sets which given user has data write access to.
     */
    List<Project> getUserDataWrite(User user);

}
