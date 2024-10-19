package org.example.trackit.Mapper;

import org.example.trackit.entity.CertifiedEquipment;
import org.example.trackit.entity.Equipment;
import org.springframework.stereotype.Component;

@Component
public class EntityMapper {

    public CertifiedEquipment toCertifiedEquipment(Equipment equipment) {
        CertifiedEquipment certifiedEquipment = new CertifiedEquipment();
        certifiedEquipment.setId(equipment.getId());
        certifiedEquipment.setPartNumber(equipment.getPartNumber());
        certifiedEquipment.setSerialNumber(equipment.getSerialNumber() + " ");
        certifiedEquipment.setHealthStatus(equipment.getHealthStatus());
        certifiedEquipment.setAllocationStatus(equipment.getAllocationStatus());
        certifiedEquipment.setJob(equipment.getJob());
        certifiedEquipment.setCreatedAt(equipment.getCreatedAt());
        certifiedEquipment.setAllocationStatusLastModified(equipment.getAllocationStatusLastModified());
        return certifiedEquipment;
    }
}
