package com.contractor.ContractorApplication.repositories;

import com.contractor.ContractorApplication.entities.ProjectStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectStatusRepository extends JpaRepository<ProjectStatus, Long> {
    List<ProjectStatus> findByIsActiveTrueOrderByName();
    Optional<ProjectStatus> findByName(String name);
    boolean existsByName(String name);
}
