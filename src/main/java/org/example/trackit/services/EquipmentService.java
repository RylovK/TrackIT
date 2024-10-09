package org.example.trackit.services;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.example.trackit.Mapper.BasicEquipmentMapper;
import org.example.trackit.Mapper.CertifiedEquipmentMapper;
import org.example.trackit.Mapper.EquipmentMapper;
import org.example.trackit.dto.BasicEquipmentDTO;
import org.example.trackit.dto.CertifiedEquipmentDTO;
import org.example.trackit.dto.EquipmentDTO;
import org.example.trackit.entity.BasicEquipment;
import org.example.trackit.entity.CertifiedEquipment;
import org.example.trackit.entity.parts.Equipment;
import org.example.trackit.entity.parts.PartNumber;
import org.example.trackit.repository.EquipmentRepository;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
public class EquipmentService {

    private final EquipmentRepository equipmentRepository;
    private final EquipmentMapper equipmentMapper;
    private final BasicEquipmentMapper basicEquipmentMapper;
    private final CertifiedEquipmentMapper certifiedEquipmentMapper;

    public List<EquipmentDTO> findAllEquipment() {
        return equipmentRepository.findAll().stream().map(equipmentMapper::toDTO).toList();
    }

    public EquipmentDTO findEquipment(EquipmentDTO equipmentDTO) {
        PartNumber partNumber = equipmentDTO.getPartNumber();
        String serialNumber = equipmentDTO.getSerialNumber();
        Optional<Equipment> equipmentOptional = equipmentRepository.findByPartNumberAndSerialNumber(partNumber, serialNumber);
        if (equipmentOptional.isPresent()) {
            Equipment equipment = equipmentOptional.get();
            if (equipment instanceof BasicEquipment basicEquipment) {
                return basicEquipmentMapper.toDTO(basicEquipment);
            } else if (equipment instanceof CertifiedEquipment certifiedEquipment) {
                return certifiedEquipmentMapper.toDTO(certifiedEquipment);
            }
        }
        throw new EntityNotFoundException("Equipment not found");
    }

    public void createEquipment(EquipmentDTO equipmentDTO) {
        System.out.print("Создаем оборудование: ");
        equipmentRepository.save(equipmentMapper.toEntity(equipmentDTO));
        if (equipmentDTO instanceof CertifiedEquipmentDTO) {
            createCertifiedEquipment(equipmentDTO);
        } else if (equipmentDTO instanceof BasicEquipmentDTO) {
            System.out.println(" Базовый тип");
            createBasicEquipment(equipmentDTO);
        }
    }

    private void createCertifiedEquipment(EquipmentDTO equipmentDTO) {
        CertifiedEquipmentDTO equipment = (CertifiedEquipmentDTO) equipmentDTO;
        CertifiedEquipment entity = certifiedEquipmentMapper.toEntity(equipment);
        equipmentRepository.save(entity);
    }

    private void createBasicEquipment(EquipmentDTO equipmentDTO) {
        System.out.println("Создаю в бд: " + equipmentDTO);
        BasicEquipmentDTO equipment = (BasicEquipmentDTO) equipmentDTO;
        BasicEquipment entity = basicEquipmentMapper.toEntity(equipment);
        equipmentRepository.save(entity);
    }
}
