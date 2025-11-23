package com.contractor.ContractorApplication.services;

import com.contractor.ContractorApplication.entities.Project;
import com.contractor.ContractorApplication.entities.ProjectStatus;
import com.contractor.ContractorApplication.entities.User;
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

    @Autowired
    private UserService userService;

    public List<Project> getAllProjects() {
        Long userId = getCurrentUserId();
        return projectRepository.findByUserId(userId);
    }

    public List<Project> getProjectsByStatus(Long statusId) {
        Long userId = getCurrentUserId();
        Optional<ProjectStatus> status = projectStatusRepository.findById(statusId);
        return status.map(projectStatus -> projectRepository.findByStatusAndUserId(projectStatus, userId))
                .orElse(List.of());
    }

    private Long getCurrentUserId() {
        Long userId = userService.getCurrentUserId();
        if (userId == null) {
            throw new RuntimeException("User not authenticated");
        }
        return userId;
    }

    public Optional<Project> getProjectById(Long id) {
        Long userId = getCurrentUserId();
        return projectRepository.findByIdAndUserId(id, userId);
    }

    public Project createProject(Project project) {
        Long userId = getCurrentUserId();

        // Get user entity and set it to project
        User user = userService.getCurrentUser()
                .orElseThrow(() -> new RuntimeException("User not found"));

        project.setUser(user);
        return projectRepository.save(project);
    }

    public Project updateProject(Long id, Project projectDetails) {
        Long userId = getCurrentUserId();
        Optional<Project> optionalProject = projectRepository.findByIdAndUserId(id, userId);

        if (optionalProject.isPresent()) {
            Project project = optionalProject.get();

            // Check if title already exists (excluding current project)
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
        Long userId = getCurrentUserId();
        Optional<Project> project = projectRepository.findByIdAndUserId(id, userId);

        if (project.isPresent()) {
            projectRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<Project> getProjectsWithSorting(String sortBy) {
        Long userId = getCurrentUserId();

        if (sortBy == null || sortBy.isEmpty()) {
            return projectRepository.findByUserId(userId);
        }

        // Implement sorting logic based on sortBy parameter
        // You'll need to add these methods to your repository
        switch (sortBy.toLowerCase()) {
            case "title":
                return projectRepository.findByUserIdOrderByTitle(userId);
            case "startdate":
                return projectRepository.findByUserIdOrderByStartDate(userId);
            case "enddate":
                return projectRepository.findByUserIdOrderByEndDate(userId);
            case "cost":
                return projectRepository.findByUserIdOrderByCost(userId);
            case "status":
                return projectRepository.findByUserIdOrderByStatusName(userId);
            default:
                return projectRepository.findByUserId(userId);
        }
    }
}