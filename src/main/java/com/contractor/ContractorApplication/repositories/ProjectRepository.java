package com.contractor.ContractorApplication.repositories;

import com.contractor.ContractorApplication.entities.Project;
import com.contractor.ContractorApplication.entities.ProjectStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> findByStatus(ProjectStatus status);
    List<Project> findByStatusId(Long statusId);

    boolean existsByTitleAndIdNot(String title, Long id);
}