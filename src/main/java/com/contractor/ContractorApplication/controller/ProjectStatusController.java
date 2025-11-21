package com.contractor.ContractorApplication.controller;

import com.contractor.ContractorApplication.entities.ProjectStatus;
import com.contractor.ContractorApplication.services.ProjectStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/project-statuses")
@CrossOrigin(origins = "*")
public class ProjectStatusController {

    @Autowired
    private ProjectStatusService projectStatusService;

    @GetMapping
    public ResponseEntity<List<ProjectStatus>> getAllProjectStatuses(
            @RequestParam(required = false) Boolean activeOnly) {

        List<ProjectStatus> statuses;
        if (activeOnly != null && activeOnly) {
            statuses = projectStatusService.getAllActiveStatuses();
        } else {
            statuses = projectStatusService.getAllStatuses();
        }
        return ResponseEntity.ok(statuses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectStatus> getProjectStatusById(@PathVariable Long id) {
        Optional<ProjectStatus> status = projectStatusService.getStatusById(id);
        return status.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ProjectStatus> createProjectStatus(@RequestBody ProjectStatus status) {
        ProjectStatus createdStatus = projectStatusService.createStatus(status);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdStatus);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectStatus> updateProjectStatus(@PathVariable Long id, @RequestBody ProjectStatus statusDetails) {
        ProjectStatus updatedStatus = projectStatusService.updateStatus(id, statusDetails);
        if (updatedStatus != null) {
            return ResponseEntity.ok(updatedStatus);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProjectStatus(@PathVariable Long id) {
        boolean deleted = projectStatusService.deleteStatus(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivateProjectStatus(@PathVariable Long id) {
        boolean deactivated = projectStatusService.deactivateStatus(id);
        if (deactivated) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
