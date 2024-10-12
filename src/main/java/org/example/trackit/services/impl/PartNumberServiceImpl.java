package org.example.trackit.services.impl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.example.trackit.Mapper.PartNumberMapper;
import org.example.trackit.dto.PartNumberDTO;
import org.example.trackit.entity.properties.PartNumber;
import org.example.trackit.repository.PartNumberRepository;
import org.example.trackit.services.PartNumberService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class PartNumberServiceImpl implements PartNumberService {

    private final PartNumberRepository partNumberRepository;
    private final PartNumberMapper partNumberMapper;

    public List<PartNumberDTO> findAllPartNumbers() {
        List<PartNumber> partNumbers = partNumberRepository.findAll();
        return partNumbers.stream().map(partNumberMapper::toDTO).toList();
    }
    public Optional<PartNumber> findPartNumberByNumber(String number) {
        return partNumberRepository.findByNumber(number);
    }

    public PartNumberDTO createPartNumber(PartNumberDTO partNumberDTO) {
        PartNumber save = partNumberRepository.save(partNumberMapper.toEntity(partNumberDTO));
        return partNumberMapper.toDTO(save);
    }
}
