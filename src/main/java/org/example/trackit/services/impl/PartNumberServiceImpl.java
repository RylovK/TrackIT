package org.example.trackit.services.impl;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.trackit.mapper.PartNumberMapper;
import org.example.trackit.dto.PartNumberDTO;
import org.example.trackit.entity.properties.PartNumber;
import org.example.trackit.repository.PartNumberRepository;
import org.example.trackit.services.PartNumberService;
import org.example.trackit.exceptions.PartNumberNotFoundException;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class PartNumberServiceImpl implements PartNumberService {

    private final PartNumberRepository partNumberRepository;
    private final PartNumberMapper partNumberMapper;

    @Override
    @Cacheable("partNumberCache")
    public List<PartNumberDTO> findAllPartNumbers() {
        return partNumberRepository.findAll().stream().map(partNumberMapper::toDTO).toList();
    }

    @Override
    public PartNumberDTO getPartNumberDTOByPartNumber(String partNumber) {
        return partNumberRepository.findByNumber(partNumber)
                .map(partNumberMapper::toDTO)
                .orElseThrow(() -> new PartNumberNotFoundException("Part number not found: " + partNumber));
    }

    @Override
    public Optional<PartNumber> findPartNumberByNumber(String number) {
        return partNumberRepository.findByNumber(number);
    }

    @Override
    @Transactional
    @CacheEvict(value = "partNumberCache", allEntries = true)
    public PartNumberDTO save(PartNumberDTO partNumberDTO) {
        PartNumber save = partNumberRepository.save(partNumberMapper.toEntity(partNumberDTO));
        log.info("Saved part number: {}", save.getNumber());
        return partNumberMapper.toDTO(save);
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    @CacheEvict(value = "partNumberCache", allEntries = true)
    public PartNumberDTO update(String existingPartNumber, PartNumberDTO dto) {
        PartNumber existing = partNumberRepository.findByNumber(existingPartNumber)
                .orElseThrow(() -> new PartNumberNotFoundException("Part number not found: " + existingPartNumber));
        if (!existingPartNumber.equalsIgnoreCase(dto.getNumber())) {
            throw new UnsupportedOperationException("Part number cannot be changed");
        } else {
            existing.setDescription(dto.getDescription());
            existing.setPhoto(dto.getPhoto());
            PartNumber saved = partNumberRepository.save(existing);
            log.info("Part number {} updated", saved.getNumber());
            return partNumberMapper.toDTO(saved);
        }
    }

    @Override
    @Transactional
    @CacheEvict(value = "partNumberCache", allEntries = true)
    public boolean deletePartNumber(String number) {
        Optional<PartNumber> founded = findPartNumberByNumber(number);
        if (founded.isPresent()) {
            log.warn("Part number {} successfully deleted", number);
            partNumberRepository.delete(founded.get());
            return true;
        }
        return false;
    }
}
