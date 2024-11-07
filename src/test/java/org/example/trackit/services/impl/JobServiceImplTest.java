package org.example.trackit.services.impl;

import org.example.trackit.Mapper.EquipmentMapper;
import org.example.trackit.Mapper.JobMapper;
import org.example.trackit.dto.EquipmentDTO;
import org.example.trackit.dto.JobDTO;
import org.example.trackit.dto.JobResponseDTO;
import org.example.trackit.entity.Equipment;
import org.example.trackit.entity.properties.AllocationStatus;
import org.example.trackit.entity.properties.HealthStatus;
import org.example.trackit.entity.properties.Job;
import org.example.trackit.exceptions.JobNotFoundException;
import org.example.trackit.repository.JobRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JobServiceImplTest {

    @Mock
    private JobRepository jobRepository;
    @Mock
    private JobMapper jobMapper;
    @Mock
    private EquipmentMapper equipmentMapper;

    @InjectMocks
    private JobServiceImpl jobService;

    private Job job;
    private JobDTO jobDTO;
    private JobResponseDTO jobResponseDTO;
    private EquipmentDTO equipmentDTO;
    private int id;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        String jobName = "test";
        job = new Job(jobName);
        job.setEquipment((List.of(new Equipment())));
        jobDTO = new JobDTO(jobName);
        jobResponseDTO = new JobResponseDTO();
        jobResponseDTO.setJobName(jobName);

        equipmentDTO = new EquipmentDTO();
        equipmentDTO.setId(1);
        equipmentDTO.setPartNumber("PN123");
        equipmentDTO.setSerialNumber("SN123");

        id = 1;
    }

    @Test
    void findAllJobs() {
        when(jobRepository.findAll()).thenReturn(List.of(job));
        when(jobMapper.toResponseDTO(job)).thenReturn(jobResponseDTO);

        List<JobResponseDTO> result = jobService.findAllJobs();
        assertNotNull(result);
        assertEquals(jobResponseDTO.getJobName(), result.getFirst().getJobName());
        assertEquals(1, result.size());

        verify(jobRepository, times(1)).findAll();
        verify(jobMapper, times(1)).toResponseDTO(job);
    }

    @Test
    void findJobById() {
        when(jobRepository.findById(id)).thenReturn(Optional.of(job));
        when(jobMapper.toJobDTO(job)).thenReturn(jobDTO);
        when(equipmentMapper.toDTO(any(Equipment.class))).thenReturn(equipmentDTO);

        JobDTO result = jobService.findJobById(id);
        assertNotNull(result);
        assertEquals(jobDTO.getJobName(), result.getJobName());
        assertEquals(1, result.getEquipment().size());
        assertEquals(equipmentDTO, result.getEquipment().getFirst());

        verify(jobRepository, times(1)).findById(id);
        verify(jobMapper, times(1)).toJobDTO(job);
        verify(equipmentMapper, times(1)).toDTO(any(Equipment.class));
    }

    @Test
    void save() {
        when(jobMapper.toJob(jobDTO)).thenReturn(job);
        when(jobRepository.save(job)).thenReturn(job);
        when(jobMapper.toJobDTO(job)).thenReturn(jobDTO);

        JobDTO result = jobService.save(jobDTO);

        assertEquals(jobDTO, result);
        verify(jobRepository, times(1)).save(job);
    }

    @Test
    void testUpdate_WhenJobExists_ReturnsUpdatedJobDTO() {
        JobDTO updated = new JobDTO("Updated");

        Job updatedJob = new Job("Updated");
        updatedJob.setEquipment(List.of(new Equipment()));

        when(jobRepository.findById(id)).thenReturn(Optional.of(job));
        when(jobRepository.save(job)).thenReturn(updatedJob);
        when(jobMapper.toJobDTO(updatedJob)).thenReturn(updated);

        JobDTO result = jobService.update(id, updated);

        assertNotNull(result);
        assertEquals(updated, result);
        assertEquals(updatedJob.getEquipment().getFirst().getJob(), updatedJob);

        verify(jobRepository, times(1)).findById(id);
        verify(jobRepository, times(1)).save(job);
        verify(jobMapper, times(1)).toJobDTO(updatedJob);
    }

    @Test
    void testUpdate_WhenJobNotFound_ThrowsJobNotFoundException() {
        int jobId = 999;
        JobDTO jobDTO = new JobDTO("Updated");

        when(jobRepository.findById(jobId)).thenReturn(Optional.empty());

        JobNotFoundException exception = assertThrows(
                JobNotFoundException.class,
                () -> jobService.update(jobId, jobDTO)
        );

        assertEquals("Job not found: " + jobId, exception.getMessage());
    }

    @Test
    void testDelete_WhenJobExists_ReturnsTrue() {
        List<Equipment> equipmentList = List.of(new Equipment(), new Equipment());
        job.setEquipment(equipmentList);

        when(jobRepository.findById(id)).thenReturn(Optional.of(job));

        boolean result = jobService.delete(id);

        assertTrue(result);
        verify(jobRepository, times(1)).delete(job);
        equipmentList.forEach(equipment -> {
            assertNull(equipment.getJob(), "Job reference in Equipment should be null");
            assertEquals(AllocationStatus.ON_BASE, equipment.getAllocationStatus(), "Allocation status should be ON_BASE");
            assertEquals(HealthStatus.RONG, equipment.getHealthStatus(), "Health status should be RONG");
        });
    }

    @Test
    void testDelete_WhenJobDoesNotExist_ReturnsFalse() {
        int jobId = 999;

        when(jobRepository.findById(jobId)).thenReturn(Optional.empty());

        boolean result = jobService.delete(jobId);

        assertFalse(result);
        verify(jobRepository, never()).delete(any());
    }
}