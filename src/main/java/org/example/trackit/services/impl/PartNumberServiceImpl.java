package org.example.trackit.services.impl;


import lombok.AllArgsConstructor;
import org.example.trackit.Mapper.PartNumberMapper;
import org.example.trackit.dto.PartNumberDTO;
import org.example.trackit.entity.Equipment;
import org.example.trackit.entity.properties.PartNumber;
import org.example.trackit.repository.PartNumberRepository;
import org.example.trackit.services.PartNumberService;
import org.example.trackit.exceptions.PartNumberNotFoundException;
import org.example.trackit.util.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class PartNumberServiceImpl implements PartNumberService {

    private final PartNumberRepository partNumberRepository;
    private final PartNumberMapper partNumberMapper;
    private final FileUtils fileUtils;

    @Override
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
    public PartNumberDTO save(PartNumberDTO partNumberDTO) {
        PartNumber save = partNumberRepository.save(partNumberMapper.toEntity(partNumberDTO));
        return partNumberMapper.toDTO(save);
    }

    @Override
    @Transactional
    public PartNumberDTO update(String existingPartNumber, PartNumberDTO dto) {
        PartNumber existing = partNumberRepository.findByNumber(existingPartNumber)
                .orElseThrow(() -> new PartNumberNotFoundException("Part number not found: " + existingPartNumber));
        if (!existingPartNumber.equalsIgnoreCase(dto.getNumber())) {
            Set<Equipment> equipmentList = existing.getEquipmentList();
            partNumberRepository.delete(existing);
            PartNumber newPartNumber  = partNumberMapper.toEntity(dto);
            newPartNumber.setEquipmentList(equipmentList);
            partNumberRepository.save(newPartNumber);
            return partNumberMapper.toDTO(newPartNumber);
        } else {
            existing.setDescription(dto.getDescription());
            existing.setPhoto(dto.getPhoto());
            PartNumber saved = partNumberRepository.save(existing);
            return partNumberMapper.toDTO(saved);
        }
    }

    @Override
    @Transactional
    public boolean deletePartNumber(String number) {
        Optional<PartNumber> founded = findPartNumberByNumber(number);
        if (founded.isPresent()) {
            partNumberRepository.delete(founded.get());
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public String saveImage(String partNumber, MultipartFile file) {
        PartNumber entity = partNumberRepository.findByNumber(partNumber)
                .orElseThrow(() -> new PartNumberNotFoundException("Part number not found"));

        String filePath = fileUtils.savePhoto(partNumber, file);
        entity.setPhoto(filePath);
        partNumberRepository.save(entity);

        return filePath;
    }

}