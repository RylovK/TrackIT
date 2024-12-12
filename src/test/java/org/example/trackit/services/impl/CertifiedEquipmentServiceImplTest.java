package org.example.trackit.services.impl;

import ch.qos.logback.classic.Logger;
import org.example.trackit.mapper.CertifiedEquipmentMapper;
import org.example.trackit.dto.CertifiedEquipmentDTO;
import org.example.trackit.entity.CertifiedEquipment;
import org.example.trackit.entity.properties.*;
import org.example.trackit.logger.EquipmentLoggerFactory;
import org.example.trackit.repository.CertifiedEquipmentRepository;
import org.example.trackit.repository.PartNumberRepository;
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
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CertifiedEquipmentServiceImplTest {

    private final Logger logger = mock(Logger.class);
    @Mock
    private CertifiedEquipmentRepository certifiedEquipmentRepository;
    @Mock
    private PartNumberRepository partNumberRepository;
    @Mock
    private CertifiedEquipmentMapper certifiedEquipmentMapper;
    @Mock
    private EquipmentLoggerFactory equipmentLoggerFactory;

    @InjectMocks
    private CertifiedEquipmentServiceImpl certifiedEquipmentServiceImpl;

    private int id;
    private PartNumber partNumber;
    private CertifiedEquipment equipment;
    private CertifiedEquipmentDTO equipmentDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        id = 1;
        this.partNumber = new PartNumber();
        partNumber.setNumber("PN123");

        this.equipment = new CertifiedEquipment();
        equipment.setId(id);
        equipment.setSerialNumber("SN123");
        equipment.setPartNumber(partNumber);

        this.equipmentDTO = new CertifiedEquipmentDTO();
        equipmentDTO.setId(id);
        equipmentDTO.setSerialNumber("SN123");
        equipmentDTO.setPartNumber("PN123");
    }

    @Test
    void findAll_ShouldReturnListOfCertifiedEquipmentDTOs() {
        when(certifiedEquipmentRepository.findAll()).thenReturn(List.of(equipment));
        when(certifiedEquipmentMapper.toDTO(equipment)).thenReturn(equipmentDTO);
        List<CertifiedEquipmentDTO> result = certifiedEquipmentServiceImpl.findAll();
        assertNotNull(result);

        assertEquals(equipmentDTO, result.getFirst());
        verify(certifiedEquipmentRepository, times(1)).findAll();
    }

    @Test
    void findAllEquipment_ShouldReturnPagedResult() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<CertifiedEquipment> page = new PageImpl<>(List.of(equipment));
        when(certifiedEquipmentRepository.findAll(pageable)).thenReturn(page);
        when(certifiedEquipmentMapper.toDTO(equipment)).thenReturn(equipmentDTO);

        Page<CertifiedEquipmentDTO> result = certifiedEquipmentServiceImpl.findAllEquipment(Collections.emptyMap(), pageable);

        assertNotNull(result);
        assertEquals(1, page.getTotalPages());
        assertEquals(equipmentDTO, result.getContent().getFirst());
        verify(certifiedEquipmentRepository, times(1)).findAll(pageable);
    }

    @Test
    void findEquipmentById_ShouldReturnCertifiedEquipmentDTO() {
        when(certifiedEquipmentRepository.findCertifiedEquipmentById(id)).thenReturn(Optional.of(equipment));
        when(certifiedEquipmentMapper.toDTO(equipment)).thenReturn(equipmentDTO);

        CertifiedEquipmentDTO result = certifiedEquipmentServiceImpl.findEquipmentById(id);

        assertNotNull(result);
        assertEquals(equipmentDTO, result);
        verify(certifiedEquipmentRepository, times(1)).findCertifiedEquipmentById(id);
        verify(certifiedEquipmentMapper, times(1)).toDTO(equipment);
    }

    @Test
    void save_ShouldSaveEquipmentAndLog() {
        when(partNumberRepository.findByNumber("PN123")).thenReturn(Optional.of(partNumber));
        when(equipmentLoggerFactory.getLogger("PN123", "SN123")).thenReturn(logger);
        when(certifiedEquipmentRepository.save(any(CertifiedEquipment.class))).thenReturn(equipment);
        when(certifiedEquipmentMapper.toDTO(equipment)).thenReturn(equipmentDTO);

        CertifiedEquipmentDTO result = certifiedEquipmentServiceImpl.save(equipmentDTO);
        assertNotNull(result);
        assertEquals(equipmentDTO, result);
        verify(certifiedEquipmentRepository, times(1)).save(any(CertifiedEquipment.class));
        verify(certifiedEquipmentMapper, times(1)).toDTO(equipment);
        verify(equipmentLoggerFactory, times(1)).getLogger("PN123", "SN123");
        verify(logger, times(1)).info("Equipment {}: {} was successfully created", result.getPartNumber(), result.getSerialNumber());
    }

    @Test
    void update_ShouldUpdateEquipmentAndLog_NewCertificationDate() {
        equipmentDTO.setCertificationDate(LocalDate.now());
        equipmentDTO.setCertificationPeriod(12);
        LocalDate nextCertificationDate = LocalDate.now().plusMonths(12);

        when(certifiedEquipmentRepository.findById(id)).thenReturn(Optional.of(equipment));
        when(partNumberRepository.findByNumber("PN123")).thenReturn(Optional.of(partNumber));

        when(certifiedEquipmentRepository.save(any(CertifiedEquipment.class))).thenReturn(equipment);
        when(certifiedEquipmentMapper.toDTO(equipment)).thenReturn(equipmentDTO);
        when(equipmentLoggerFactory.getLogger("PN123", "SN123")).thenReturn(logger);

        CertifiedEquipmentDTO result = certifiedEquipmentServiceImpl.update(id, equipmentDTO);

        assertNotNull(result);
        assertEquals(equipmentDTO, result);

        assertEquals(CertificationStatus.VALID, equipment.getCertificationStatus());
        assertEquals(LocalDate.now(), equipment.getCertificationDate());
        assertEquals(12, equipment.getCertificationPeriod());
        assertEquals(nextCertificationDate, equipment.getNextCertificationDate());

        verify(certifiedEquipmentRepository, times(1)).findById(id);
        verify(partNumberRepository, times(1)).findByNumber("PN123");
        verify(certifiedEquipmentRepository, times(1)).save(equipment);
        verify(certifiedEquipmentMapper, times(1)).toDTO(equipment);
        verify(equipmentLoggerFactory, times(1)).getLogger("PN123", "SN123");
        verify(logger, times(1)).info("Certification date was updated. Next certification date: {}", nextCertificationDate);
    }

    @Test
    void deleteEquipmentById() {
        when(certifiedEquipmentRepository.findById(id)).thenReturn(Optional.of(equipment));
        when(equipmentLoggerFactory.getLogger("PN123", "SN123")).thenReturn(logger);

        boolean result = certifiedEquipmentServiceImpl.deleteEquipmentById(id);
        assertTrue(result);

        verify(certifiedEquipmentRepository, times(1)).delete(equipment);
        verify(logger, times(1)).info("Equipment {}: {} was successfully deleted", "SN123", "PN123");
    }
}