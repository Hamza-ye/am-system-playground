package org.nmcpye.activitiesmanagement.extended.project;

import org.nmcpye.activitiesmanagement.domain.User;
import org.nmcpye.activitiesmanagement.domain.project.Project;
import org.nmcpye.activitiesmanagement.extended.project.pagingrepository.ProjectPagingRepository;

import java.util.List;

/**
 * Created by Hamza on 17/11/2021.
 */
public class DefaultProjectService implements ProjectServiceExt {
    private final ProjectPagingRepository projectPagingRepository;

    public DefaultProjectService(ProjectPagingRepository projectPagingRepository) {
        this.projectPagingRepository = projectPagingRepository;
    }

    @Override
    public long addProject(Project dengueCasesReport) {
        projectPagingRepository.saveObject(dengueCasesReport);
        return dengueCasesReport.getId();
    }

    @Override
    public void updateProject(Project dengueCasesReport) {
        projectPagingRepository.update(dengueCasesReport);
    }

    @Override
    public void deleteProject(Project dengueCasesReport) {
        projectPagingRepository.delete(dengueCasesReport);
    }

    @Override
    public Project getProject(long id) {
        return projectPagingRepository.get(id);
    }

    @Override
    public Project getProject(String uid) {
        return projectPagingRepository.getByUid(uid);
    }

    @Override
    public Project getProjectNoAcl(String uid) {
        return projectPagingRepository.getByUidNoAcl(uid);
    }

    @Override
    public List<Project> getAllProjects() {
        return projectPagingRepository.getAll();
    }

    @Override
    public List<Project> getAllDataRead() {
        return projectPagingRepository.getDataReadAll();
    }

    @Override
    public List<Project> getUserDataRead(User user) {
        return projectPagingRepository.getDataReadAll(user);
    }

    @Override
    public List<Project> getAllDataWrite() {
        return projectPagingRepository.getDataWriteAll();
    }

    @Override
    public List<Project> getUserDataWrite(User user) {
        return projectPagingRepository.getDataWriteAll(user);
    }
}
