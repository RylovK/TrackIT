package org.example.trackit.services.impl;

import org.example.trackit.Mapper.PartNumberMapper;
import org.example.trackit.dto.PartNumberDTO;
import org.example.trackit.entity.properties.PartNumber;
import org.example.trackit.repository.PartNumberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PartNumberServiceImplTest {

    @Mock
    private PartNumberRepository partNumberRepository;
    @Mock
    private PartNumberMapper partNumberMapper;

    @InjectMocks
    private PartNumberServiceImpl partNumberService;

    private PartNumber partNumber;
    private PartNumberDTO partNumberDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        partNumber = new PartNumber("PN123");
        partNumberDTO = new PartNumberDTO();
        partNumberDTO.setNumber("PN123");

    }

    @Test
    void findAllPartNumbers() {
        when(partNumberRepository.findAll()).thenReturn(List.of(partNumber));
        when(partNumberMapper.toDTO(partNumber)).thenReturn(partNumberDTO);

        List<PartNumberDTO> result = partNumberService.findAllPartNumbers();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(partNumberDTO.getNumber(), result.getFirst().getNumber());

        verify(partNumberRepository, times(1)).findAll();
        verify(partNumberMapper, times(1)).toDTO(partNumber);
    }

    @Test
    void getPartNumberDTOByPartNumber() {
        when(partNumberRepository.findByNumber("PN123")).thenReturn(Optional.ofNullable(partNumber));
        when(partNumberMapper.toDTO(partNumber)).thenReturn(partNumberDTO);

        PartNumberDTO result = partNumberService.getPartNumberDTOByPartNumber("PN123");
        assertNotNull(result);
        assertEquals(partNumberDTO.getNumber(), result.getNumber());
        verify(partNumberRepository, times(1)).findByNumber("PN123");
        verify(partNumberMapper, times(1)).toDTO(partNumber);
    }

    @Test
    void findPartNumberByNumber() {
        when(partNumberRepository.findByNumber("PN123")).thenReturn(Optional.ofNullable(partNumber));

        Optional<PartNumber> result = partNumberRepository.findByNumber("PN123");
        assertNotNull(result);
        assertEquals(partNumberDTO.getNumber(), result.get().getNumber());
        verify(partNumberRepository, times(1)).findByNumber("PN123");
    }

    @Test
    void save() {
        when(partNumberMapper.toEntity(partNumberDTO)).thenReturn(partNumber);
        when(partNumberRepository.save(partNumber)).thenReturn(partNumber);
        when(partNumberMapper.toDTO(partNumber)).thenReturn(partNumberDTO);

        PartNumberDTO result = partNumberService.save(partNumberDTO);
        assertNotNull(result);
        assertEquals(partNumberDTO.getNumber(), result.getNumber());
        verify(partNumberRepository, times(1)).save(partNumber);
        verify(partNumberMapper, times(1)).toDTO(partNumber);
        verify(partNumberMapper, times(1)).toEntity(partNumberDTO);
    }

    @Test
    void update_WhenChangedNumber_ShouldThrowUnsupportedOperationException() {
        when(partNumberRepository.findByNumber("PN123")).thenReturn(Optional.ofNullable(partNumber));
        String existingNumber = partNumber.getNumber();
        PartNumberDTO dtoWithChangedNumber = partNumberDTO;
        dtoWithChangedNumber.setNumber("CHANGED NUMBER");

        UnsupportedOperationException exception = assertThrows(UnsupportedOperationException.class,
                () -> partNumberService.update(existingNumber, dtoWithChangedNumber));
        assertEquals("Part number cannot be changed", exception.getMessage());
        verify(partNumberRepository, times(1)).findByNumber("PN123");
    }

    @Test
    void update_ShouldUpdatePartNumber() {
        partNumberDTO.setDescription("Updated Description");
        partNumberDTO.setPhoto("Updated Photo");

        when(partNumberRepository.findByNumber("PN123")).thenReturn(Optional.ofNullable(partNumber));
        when(partNumberRepository.save(partNumber)).thenReturn(partNumber);
        when(partNumberMapper.toDTO(partNumber)).thenReturn(partNumberDTO);

        PartNumberDTO result = partNumberService.update("PN123", partNumberDTO);

        assertNotNull(result);
        assertEquals(partNumberDTO, result);
        assertEquals(partNumberDTO.getDescription(), partNumber.getDescription());
        assertEquals(partNumberDTO.getPhoto(), partNumber.getPhoto());
        verify(partNumberRepository, times(1)).findByNumber("PN123");
        verify(partNumberRepository, times(1)).save(partNumber);
        verify(partNumberMapper, times(1)).toDTO(partNumber);
    }

    @Test
    void deletePartNumber_ValidNumber_ShouldReturnTrue() {
        when(partNumberRepository.findByNumber("PN123")).thenReturn(Optional.of(partNumber));
        boolean deleted = partNumberService.deletePartNumber("PN123");
        assertTrue(deleted);
        verify(partNumberRepository, times(1)).findByNumber("PN123");
    }

    @Test
    void deletePartNumber_InvalidNumber_ShouldReturnFalse() {
        when(partNumberRepository.findByNumber("WRONG")).thenReturn(Optional.empty());
        boolean deleted = partNumberService.deletePartNumber("WRONG");
        assertFalse(deleted);
        verify(partNumberRepository, times(1)).findByNumber("WRONG");
    }
}