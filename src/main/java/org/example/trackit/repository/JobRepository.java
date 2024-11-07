package org.example.trackit.repository;

import org.example.trackit.entity.properties.Job;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JobRepository extends JpaRepository<Job, Integer> {
    Optional<Job> findByJobName(String jobName);
}
