package com.contractor.ContractorApplication.services;

import com.contractor.ContractorApplication.entities.Project;
import com.contractor.ContractorApplication.entities.ProjectStatus;
import com.contractor.ContractorApplication.repositories.ProjectRepository;
import com.contractor.ContractorApplication.repositories.ProjectStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectStatusRepository projectStatusRepository;

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public List<Project> getProjectsByStatus(Long statusId) {
        Optional<ProjectStatus> status = projectStatusRepository.findById(statusId);
        return status.map(projectStatus -> projectRepository.findByStatus(projectStatus))
                .orElse(List.of());
    }

    public Optional<Project> getProjectById(Long id) {
        return projectRepository.findById(id);
    }

    public Project createProject(Project project) {
        return projectRepository.save(project);
    }

    public Project updateProject(Long id, Project projectDetails) {
        Optional<Project> optionalProject = projectRepository.findById(id);
        if (optionalProject.isPresent()) {
            Project project = optionalProject.get();
            if (!project.getTitle().equals(projectDetails.getTitle()) &&
                    projectRepository.existsByTitleAndIdNot(projectDetails.getTitle(), id)) {
                throw new RuntimeException("Project title already exists");
            }
            project.setTitle(projectDetails.getTitle());
            project.setStatus(projectDetails.getStatus());
            project.setStartDate(projectDetails.getStartDate());
            project.setEndDate(projectDetails.getEndDate());
            project.setCost(projectDetails.getCost());
            return projectRepository.save(project);
        }
        return null;
    }

    public boolean deleteProject(Long id) {
        if (projectRepository.existsById(id)) {
            projectRepository.deleteById(id);
            return true;
        }
        return false;
    }
}