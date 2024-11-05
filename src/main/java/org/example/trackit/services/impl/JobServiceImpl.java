package org.example.trackit.services.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.trackit.Mapper.EquipmentMapper;
import org.example.trackit.Mapper.JobMapper;
import org.example.trackit.dto.EquipmentDTO;
import org.example.trackit.dto.JobDTO;
import org.example.trackit.dto.JobResponseDTO;
import org.example.trackit.entity.properties.Job;
import org.example.trackit.repository.JobRepository;
import org.example.trackit.services.JobService;
import org.example.trackit.exceptions.JobNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;
    private final JobMapper jobMapper;
    private final EquipmentMapper equipmentMapper;

    @Override
    public List<JobResponseDTO> findAllJobs() {
        return jobRepository.findAll().stream().map(jobMapper::toResponseDTO).toList();
    }

    @Override
    public JobDTO findJobById(int id) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new JobNotFoundException("Job not found: " + id));
        JobDTO jobDTO = jobMapper.toJobDTO(job);
        Set<EquipmentDTO> equipment = job.getEquipment().stream()
                .map(equipmentMapper::toDTO)
                .collect(Collectors.toSet());
        jobDTO.setEquipment(equipment);
        return jobDTO;
    }


    @Override
    @Transactional
    public JobDTO save(JobDTO jobDTO) {
        Job saved = jobRepository.save(jobMapper.toJob(jobDTO));
        log.info("Job saved: {}", saved.getJobName());
        return jobMapper.toJobDTO(saved);
    }

    @Override
    @Transactional
    public JobDTO update(int id, JobDTO jobDTO) {
        Job existing = jobRepository.findById(id).orElseThrow(() -> new JobNotFoundException("Job not found: " + id));
        existing.setJobName(jobDTO.getJobName());
        Job updated = jobRepository.save(existing);
        updated.getEquipment().forEach(equipment -> equipment.setJob(updated));
        log.info("Job updated: {}", updated.getJobName());
        return jobMapper.toJobDTO(updated);
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        if (jobRepository.existsById(id)) {
            jobRepository.deleteById(id);
            log.info("Job deleted: {}", id);
            return true;
        }
        return false;
    }

}
