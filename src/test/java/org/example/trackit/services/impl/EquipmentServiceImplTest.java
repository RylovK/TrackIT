package org.example.trackit.services.impl;

import ch.qos.logback.classic.Logger;
import org.example.trackit.Mapper.EquipmentMapper;
import org.example.trackit.dto.CertifiedEquipmentDTO;
import org.example.trackit.dto.EquipmentDTO;
import org.example.trackit.entity.CertifiedEquipment;
import org.example.trackit.entity.Equipment;
import org.example.trackit.entity.properties.AllocationStatus;
import org.example.trackit.entity.properties.HealthStatus;
import org.example.trackit.entity.properties.Job;
import org.example.trackit.entity.properties.PartNumber;
import org.example.trackit.logger.EquipmentLoggerFactory;
import org.example.trackit.repository.EquipmentRepository;
import org.example.trackit.repository.JobRepository;
import org.example.trackit.repository.PartNumberRepository;
import org.example.trackit.services.EquipmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EquipmentServiceImplTest {

    private final Logger logger = mock(Logger.class);
    @Mock
    private EquipmentRepository equipmentRepository;
    @Mock
    private PartNumberRepository partNumberRepository;
    @Mock
    private JobRepository jobRepository;
    @Mock
    private EquipmentMapper equipmentMapper;
    @Mock
    private EquipmentService<CertifiedEquipmentDTO> certifiedEquipmentServiceImpl;
    @Mock
    private EquipmentLoggerFactory equipmentLoggerFactory;

    @InjectMocks
    private EquipmentServiceImpl equipmentServiceImpl;

    private Equipment equipment;
    private EquipmentDTO equipmentDTO;
    private Job job;
    private PartNumber partNumber;
    private int id;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        id = 1;
        this.partNumber = new PartNumber();
        partNumber.setNumber("PN123");

        this.equipment = new Equipment();
        equipment.setId(id);
        equipment.setSerialNumber("SN123");
        equipment.setPartNumber(partNumber);

        this.equipmentDTO = new EquipmentDTO();
        equipmentDTO.setId(id);
        equipmentDTO.setSerialNumber("SN123");
        equipmentDTO.setPartNumber("PN123");

        this.job = new Job();
        job.setId(id);
        job.setJobName("Job123");
        job.setEquipment(new HashSet<>());
    }

    @Test
    void findAll_ShouldReturnListOfEquipmentDTOs() {
        when(equipmentRepository.findAll()).thenReturn(List.of(equipment));
        when(equipmentMapper.toDTO(equipment)).thenReturn(equipmentDTO);

        List<EquipmentDTO> result = equipmentServiceImpl.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(equipmentDTO, result.getFirst());
        verify(equipmentRepository, times(1)).findAll();
    }


    @Test
    void findAllEquipment_ShouldReturnPagedResult() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Equipment> equipmentPage = new PageImpl<>(List.of(equipment));

        when(equipmentRepository.findAll(pageable)).thenReturn(equipmentPage);
        when(equipmentMapper.toDTO(equipment)).thenReturn(equipmentDTO);

        Page<EquipmentDTO> result = equipmentServiceImpl.findAllEquipment(Collections.emptyMap(), pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(equipmentDTO, result.getContent().getFirst());
        verify(equipmentRepository, times(1)).findAll(pageable);
    }


    @Test
    void save_ShouldSaveEquipmentAndLog() {
        when(partNumberRepository.findByNumber("PN123")).thenReturn(Optional.of(partNumber));
        when(equipmentRepository.save(any(Equipment.class))).thenReturn(equipment);
        when(equipmentLoggerFactory.getLogger("PN123", "SN123")).thenReturn(logger);
        when(equipmentMapper.toDTO(equipment)).thenReturn(equipmentDTO);

        EquipmentDTO result = equipmentServiceImpl.save(equipmentDTO);

        assertEquals(equipmentDTO, result);
        verify(equipmentRepository, times(1)).save(any(Equipment.class));
        verify(logger, times(1)).info("Equipment {}: {} was successfully created", "PN123", "SN123");
    }

    @Test
    void update_ShouldUpdateEquipmentAndLog_ShipmentToJob() {
        equipment.setHealthStatus(HealthStatus.RONG);
        equipment.setAllocationStatus(AllocationStatus.ON_BASE);

        equipmentDTO.setAllocationStatus(AllocationStatus.ON_LOCATION);
        equipmentDTO.setJobName(job.getJobName());
        equipmentDTO.setHealthStatus(HealthStatus.RITE);

        when(equipmentRepository.findById(id)).thenReturn(Optional.of(equipment));
        when(partNumberRepository.findByNumber("PN123")).thenReturn(Optional.of(partNumber));
        when(jobRepository.findByJobName("Job123")).thenReturn(Optional.of(job));
        when(equipmentRepository.save(any(Equipment.class))).thenReturn(equipment);
        when(equipmentMapper.toDTO(equipment)).thenReturn(equipmentDTO);
        when(equipmentLoggerFactory.getLogger("PN123", "SN123")).thenReturn(logger);

        EquipmentDTO result = equipmentServiceImpl.update(id, equipmentDTO);

        assertNotNull(result);
        assertEquals(job.getJobName(), equipment.getLastJob());
        assertEquals(job, equipment.getJob());
        assertEquals(HealthStatus.RITE, equipment.getHealthStatus());
        assertEquals(AllocationStatus.ON_LOCATION, equipment.getAllocationStatus());
        assertEquals(LocalDate.now(), equipment.getAllocationStatusLastModified());

        verify(equipmentRepository, times(1)).findById(id);
        verify(partNumberRepository, times(1)).findByNumber("PN123");
        verify(jobRepository, times(1)).findByJobName("Job123");
        verify(equipmentRepository, times(1)).save(equipment);
        verify(equipmentMapper, times(1)).toDTO(equipment);

        verify(equipmentLoggerFactory, times(1)).getLogger("PN123", "SN123");
        verify(logger, times(1))
                .info("Health status updated from {} to {}", HealthStatus.RONG, HealthStatus.RITE);
        verify(logger, times(1))
                .info( "Allocation status updated from {} to {}", AllocationStatus.ON_BASE, AllocationStatus.ON_LOCATION);
        verify(logger, times(1)).info(  "Equipment was send to job: {}", job.getJobName());
    }

    @Test
    void update_ShouldUpdateEquipmentAndLog_ReturnFromJob() {
        equipment.setHealthStatus(HealthStatus.RITE);
        equipment.setAllocationStatus(AllocationStatus.ON_LOCATION);
        equipment.setJob(job);

        equipmentDTO.setAllocationStatus(AllocationStatus.ON_BASE);

        when(equipmentRepository.findById(id)).thenReturn(Optional.of(equipment));
        when(partNumberRepository.findByNumber("PN123")).thenReturn(Optional.of(partNumber));
        when(equipmentRepository.save(any(Equipment.class))).thenReturn(equipment);
        when(equipmentMapper.toDTO(equipment)).thenReturn(equipmentDTO);
        when(equipmentLoggerFactory.getLogger("PN123", "SN123")).thenReturn(logger);

        EquipmentDTO result = equipmentServiceImpl.update(id, equipmentDTO);

        assertNotNull(result);
        assertEquals(HealthStatus.RONG, equipment.getHealthStatus());
        assertEquals(HealthStatus.RONG, equipmentDTO.getHealthStatus());
        assertNull(equipment.getJob());

        verify(equipmentRepository, times(1)).findById(id);
        verify(partNumberRepository, times(1)).findByNumber("PN123");
        verify(jobRepository, times(0)).findByJobName("Job123");
        verify(equipmentRepository, times(1)).save(equipment);
        verify(equipmentMapper, times(1)).toDTO(equipment);
        verify(equipmentLoggerFactory, times(1)).getLogger("PN123", "SN123");

        verify(logger, times(1))
                .info("Health status updated from {} to {}", HealthStatus.RITE, HealthStatus.RONG);
        verify(logger, times(1))
                .info("Allocation status updated from {} to {}", AllocationStatus.ON_LOCATION, AllocationStatus.ON_BASE);
    }

    @Test
    void findEquipmentById_ShouldReturnEquipmentDTO() {
        when(equipmentRepository.findById(id)).thenReturn(Optional.of(equipment));
        when(equipmentMapper.toDTO(equipment)).thenReturn(equipmentDTO);

        EquipmentDTO result = equipmentServiceImpl.findEquipmentById(id);

        assertNotNull(result);
        assertEquals(equipmentDTO, result);
        verify(equipmentRepository, times(1)).findById(eq(id));
        verify(equipmentMapper, times(1)).toDTO(equipment);
    }

    @Test
    void findEquipmentById_ShouldReturnCertifiedEquipmentDTO_WhenExists() {
        CertifiedEquipment equipment = new CertifiedEquipment();
        CertifiedEquipmentDTO certifiedEquipmentDTO = new CertifiedEquipmentDTO();
        when(equipmentRepository.findById(id)).thenReturn(Optional.of(equipment));
        when(certifiedEquipmentServiceImpl.findEquipmentById(id)).thenReturn(certifiedEquipmentDTO);

        EquipmentDTO result = equipmentServiceImpl.findEquipmentById(id);
        assertEquals(certifiedEquipmentDTO, result);
        verify(equipmentRepository, times(1)).findById(eq(id));
        verify(certifiedEquipmentServiceImpl, times(1)).findEquipmentById(eq(id));
    }


    @Test
    void deleteEquipmentById_ShouldDeleteAndLog_WhenExists() {
        when(equipmentRepository.findById(id)).thenReturn(Optional.of(equipment));
        when(equipmentLoggerFactory.getLogger("PN123", "SN123")).thenReturn(logger);

        boolean result = equipmentServiceImpl.deleteEquipmentById(id);
        assertTrue(result);

        verify(equipmentRepository, times(1)).delete(equipment);
        verify(logger, times(1)).info("Equipment {}: {} was successfully deleted", "SN123", "PN123");
    }

}