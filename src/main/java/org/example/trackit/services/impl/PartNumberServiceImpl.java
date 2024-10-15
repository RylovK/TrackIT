package org.example.trackit.services.impl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.example.trackit.Mapper.PartNumberMapper;
import org.example.trackit.dto.EquipmentDTO;
import org.example.trackit.dto.PartNumberDTO;
import org.example.trackit.entity.properties.PartNumber;
import org.example.trackit.repository.PartNumberRepository;
import org.example.trackit.services.PartNumberService;
import org.example.trackit.util.exceptions.PartNumberNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class PartNumberServiceImpl implements PartNumberService {

    private final PartNumberRepository partNumberRepository;
    private final PartNumberMapper partNumberMapper;

    @Override
    public Page<PartNumberDTO> findAllPartNumbers(Pageable pageable) {
        return partNumberRepository.findAll(pageable).map(partNumberMapper::toDTO);
    }

    @Override
    public PartNumberDTO getPartNumberDTOByPartNumber(String partNumber) {
        Optional<PartNumber> byNumber = partNumberRepository.findByNumber(partNumber);
        if (byNumber.isEmpty()) {
            throw new PartNumberNotFoundException(partNumber);
        }
        return partNumberMapper.toDTO(byNumber.get());
    }

    @Override
    public Optional<PartNumber> findPartNumberByNumber(String number) {
        return partNumberRepository.findByNumber(number);
    }

    @Override
    public PartNumberDTO save(PartNumberDTO partNumberDTO) {
        PartNumber save = partNumberRepository.save(partNumberMapper.toEntity(partNumberDTO));
        return partNumberMapper.toDTO(save);
    }

    @Override
    public boolean deletePartNumber(String number) {
        Optional<PartNumber> partNumberByNumber = findPartNumberByNumber(number);
        if (partNumberByNumber.isPresent()) {
            partNumberRepository.delete(partNumberByNumber.get());
            return true;
        }
        return false;
    }
}
