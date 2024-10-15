package org.example.trackit.services.impl;

import lombok.AllArgsConstructor;
import org.example.trackit.Mapper.JobMapper;
import org.example.trackit.dto.JobDTO;
import org.example.trackit.entity.properties.Job;
import org.example.trackit.repository.JobRepository;
import org.example.trackit.services.JobService;
import org.example.trackit.exceptions.JobNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;
    private final JobMapper jobMapper;

    @Override
    public List<JobDTO> findAllJobs() {
        return jobRepository.findAll().stream().map(jobMapper::toJobDTO).toList();
    }

    @Override
    public JobDTO findJobById(int id) {
        return jobRepository.findById(id)
                .map(jobMapper::toJobDTO)
                .orElseThrow(() -> new JobNotFoundException("Job not found: " + id));
    }


    @Override
    @Transactional
    public JobDTO save(JobDTO jobDTO) {
        Job saved = jobRepository.save(jobMapper.toJob(jobDTO));
        return jobMapper.toJobDTO(saved);
    }

    @Override
    @Transactional
    public JobDTO update(int id, JobDTO jobDTO) {
        Job existing = jobRepository.findById(id).orElseThrow(() -> new JobNotFoundException("Job not found: " + id));
        existing.setJobName(jobDTO.getJobName());
        Job updated = jobRepository.save(existing);
        return jobMapper.toJobDTO(updated);
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        if (jobRepository.existsById(id)) {
            jobRepository.deleteById(id);
            return true;
        }
        return false;
    }

}
