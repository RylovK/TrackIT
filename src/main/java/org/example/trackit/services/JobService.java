package org.example.trackit.services;

import org.example.trackit.dto.JobDTO;
import org.example.trackit.dto.JobResponseDTO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface JobService {

    List<JobResponseDTO> findAllJobs();

    JobDTO findJobById(int id);

    @Transactional
    JobDTO save(JobDTO jobDTO);

    @Transactional
    JobDTO update(int id, JobDTO jobDTO);

    @Transactional
    boolean delete(int id);
}
