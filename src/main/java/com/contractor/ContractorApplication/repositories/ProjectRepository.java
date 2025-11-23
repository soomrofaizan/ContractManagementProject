package com.contractor.ContractorApplication.repositories;

import com.contractor.ContractorApplication.entities.Project;
import com.contractor.ContractorApplication.entities.ProjectStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> findByStatus(ProjectStatus status);
    List<Project> findByStatusId(Long statusId);
    boolean existsByTitleAndIdNot(String title, Long id);

    // NEW METHODS FOR USER-SPECIFIC QUERIES
    List<Project> findByUserId(Long userId);
    List<Project> findByStatusAndUserId(ProjectStatus status, Long userId);
    Optional<Project> findByIdAndUserId(Long id, Long userId);

    List<Project> findByUserIdOrderByTitle(Long userId);
    List<Project> findByUserIdOrderByStartDate(Long userId);
    List<Project> findByUserIdOrderByEndDate(Long userId);
    List<Project> findByUserIdOrderByCost(Long userId);
    List<Project> findByUserIdOrderByStatusName(Long userId);

    // Additional utility methods
    boolean existsByTitleAndUserId(String title, Long userId);
    long countByUserId(Long userId);
}