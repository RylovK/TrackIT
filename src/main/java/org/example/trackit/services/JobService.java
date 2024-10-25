package org.example.trackit.services;

import org.example.trackit.dto.JobDTO;
import org.example.trackit.dto.JobResponseDTO;

import java.util.List;

public interface JobService {

    List<JobResponseDTO> findAllJobs();

    JobDTO findJobById(int id);

    JobDTO save(JobDTO jobDTO);

    JobDTO update(int id, JobDTO jobDTO);

    boolean delete(int id);
}
