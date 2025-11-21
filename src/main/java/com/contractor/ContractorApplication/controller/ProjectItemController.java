package com.contractor.ContractorApplication.controller;

import com.contractor.ContractorApplication.entities.ProjectItem;
import com.contractor.ContractorApplication.services.ProjectItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

        import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/projects/{projectId}/items")
@CrossOrigin(origins = "*")
public class ProjectItemController {

    @Autowired
    private ProjectItemService projectItemService;

    @GetMapping
    public ResponseEntity<List<ProjectItem>> getProjectItems(@PathVariable Long projectId) {
        List<ProjectItem> items = projectItemService.getItemsByProjectId(projectId);
        return ResponseEntity.ok(items);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<ProjectItem> getItemById(@PathVariable Long projectId,
                                                   @PathVariable Long itemId) {
        Optional<ProjectItem> item = projectItemService.getItemById(itemId);
        return item.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ProjectItem> addItemToProject(@PathVariable Long projectId, @RequestBody ProjectItem item) {
        ProjectItem createdItem = projectItemService.addItemToProject(projectId, item);
        if (createdItem != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(createdItem);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{itemId}")
    public ResponseEntity<ProjectItem> updateItem(@PathVariable Long projectId, @PathVariable Long itemId, @RequestBody ProjectItem itemDetails) {
        ProjectItem updatedItem = projectItemService.updateItem(itemId, itemDetails);
        if (updatedItem != null) {
            return ResponseEntity.ok(updatedItem);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long projectId,
                                           @PathVariable Long itemId) {
        boolean deleted = projectItemService.deleteItem(itemId);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
