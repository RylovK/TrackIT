package org.example.trackit.services.impl;

import lombok.AllArgsConstructor;
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

        // If there are nested mappings (e.g., for equipment)
        Set<EquipmentDTO> equipmentDTOs = job.getEquipment().stream()
                .map(equipmentMapper::toDTO) // Ensure you're using the correct mapper
                .collect(Collectors.toSet());

        jobDTO.setEquipment(equipmentDTOs);
        return jobDTO;
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
        updated.getEquipment().forEach(equipment -> equipment.setJob(updated));
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
