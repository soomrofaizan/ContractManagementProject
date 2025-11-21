package com.contractor.ContractorApplication.services;

import com.contractor.ContractorApplication.entities.Project;
import com.contractor.ContractorApplication.entities.ProjectItem;
import com.contractor.ContractorApplication.repositories.ProjectItemRepository;
import com.contractor.ContractorApplication.repositories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectItemService {

    @Autowired
    private ProjectItemRepository projectItemRepository;

    @Autowired
    private ProjectRepository projectRepository;

    public List<ProjectItem> getItemsByProjectId(Long projectId) {
        return projectItemRepository.findByProjectId(projectId);
    }

    public Optional<ProjectItem> getItemById(Long id) {
        return projectItemRepository.findById(id);
    }

    public ProjectItem addItemToProject(Long projectId, ProjectItem item) {
        Optional<Project> optionalProject = projectRepository.findById(projectId);
        if (optionalProject.isPresent()) {
            Project project = optionalProject.get();
            item.setProject(project);
            return projectItemRepository.save(item);
        }
        return null;
    }

    public ProjectItem updateItem(Long id, ProjectItem itemDetails) {
        Optional<ProjectItem> optionalItem = projectItemRepository.findById(id);
        if (optionalItem.isPresent()) {
            ProjectItem item = optionalItem.get();
            item.setDate(itemDetails.getDate());
            item.setItem(itemDetails.getItem());
            item.setRate(itemDetails.getRate());
            item.setQuantity(itemDetails.getQuantity());
            item.setRent(itemDetails.getRent());
            item.setTotalAmount(itemDetails.getTotalAmount());
            return projectItemRepository.save(item);
        }
        return null;
    }

    public boolean deleteItem(Long id) {
        if (projectItemRepository.existsById(id)) {
            projectItemRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
