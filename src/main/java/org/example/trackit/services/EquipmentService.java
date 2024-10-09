package org.example.trackit.services;

import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import org.example.trackit.Mapper.CertifiedEquipmentMapper;
import org.example.trackit.Mapper.EquipmentMapper;
import org.example.trackit.dto.CertifiedEquipmentDTO;
import org.example.trackit.dto.EquipmentDTO;
import org.example.trackit.entity.CertifiedEquipment;
import org.example.trackit.entity.Equipment;
import org.example.trackit.entity.properties.PartNumber;
import org.example.trackit.repository.EquipmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
public class EquipmentService {

    private final EquipmentRepository equipmentRepository;
    private final EquipmentMapper equipmentMapper;
    private final CertifiedEquipmentMapper certifiedEquipmentMapper;
    private final PartNumberService partNumberService;

    public List<EquipmentDTO> findAllEquipment() {
        return equipmentRepository.findAll().stream().map(equipmentMapper::toDTO).toList();
    }

    public EquipmentDTO createEquipment(EquipmentDTO equipmentDTO) {
        Optional<PartNumber> partNumber = partNumberService.findPartNumberByPartNumber(equipmentDTO.getPartNumber());
        Equipment equipment = new Equipment(partNumber.get(), equipmentDTO.getSerialNumber());
        partNumber.get().getEquipmentList().add(equipment);
        equipmentRepository.save(equipment);
        return equipmentMapper.toDTO(equipment);
    }

    public EquipmentDTO findEquipmentById(int id) {
        Optional<Equipment> byId = equipmentRepository.findById(id);
        return byId.map(equipmentMapper::toDTO).orElse(null);//TODO:сделать exception?
    }

    public List<CertifiedEquipmentDTO> findAllCertifiedEquipment() {
        List<CertifiedEquipment> founded = equipmentRepository.findAllCertifiedEquipment();
        return founded.stream().map(certifiedEquipmentMapper::toDTO).toList();
    }
    public Optional<Equipment> findByPartNumberAndSerialNumber(@NotEmpty String partNumber, String serialNumber) {
        return equipmentRepository.findByPartNumberAndSerialNumber(partNumber, serialNumber);
    }
}
