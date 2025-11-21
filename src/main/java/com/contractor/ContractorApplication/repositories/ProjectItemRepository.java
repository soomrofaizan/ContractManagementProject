package com.contractor.ContractorApplication.repositories;

import com.contractor.ContractorApplication.entities.ProjectItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectItemRepository extends JpaRepository<ProjectItem, Long> {
    List<ProjectItem> findByProjectId(Long projectId);
    void deleteByProjectId(Long projectId);
}
