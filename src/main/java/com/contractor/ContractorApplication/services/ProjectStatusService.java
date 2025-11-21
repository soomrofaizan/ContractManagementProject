package com.contractor.ContractorApplication.services;

import com.contractor.ContractorApplication.entities.ProjectStatus;
import com.contractor.ContractorApplication.repositories.ProjectStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectStatusService {

    @Autowired
    private ProjectStatusRepository projectStatusRepository;

    public List<ProjectStatus> getAllActiveStatuses() {
        return projectStatusRepository.findByIsActiveTrueOrderByName();
    }

    public List<ProjectStatus> getAllStatuses() {
        return projectStatusRepository.findAll();
    }

    public Optional<ProjectStatus> getStatusById(Long id) {
        return projectStatusRepository.findById(id);
    }

    public Optional<ProjectStatus> getStatusByName(String name) {
        return projectStatusRepository.findByName(name);
    }

    public ProjectStatus createStatus(ProjectStatus status) {
        return projectStatusRepository.save(status);
    }

    public ProjectStatus updateStatus(Long id, ProjectStatus statusDetails) {
        Optional<ProjectStatus> optionalStatus = projectStatusRepository.findById(id);
        if (optionalStatus.isPresent()) {
            ProjectStatus status = optionalStatus.get();
            status.setName(statusDetails.getName());
            status.setDescription(statusDetails.getDescription());
            status.setIsActive(statusDetails.getIsActive());
            return projectStatusRepository.save(status);
        }
        return null;
    }

    public boolean deleteStatus(Long id) {
        if (projectStatusRepository.existsById(id)) {
            projectStatusRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public boolean deactivateStatus(Long id) {
        Optional<ProjectStatus> optionalStatus = projectStatusRepository.findById(id);
        if (optionalStatus.isPresent()) {
            ProjectStatus status = optionalStatus.get();
            status.setIsActive(false);
            projectStatusRepository.save(status);
            return true;
        }
        return false;
    }
}