package org.example.trackit.Mapper;

import org.example.trackit.dto.CertifiedEquipmentDTO;
import org.example.trackit.entity.CertifiedEquipment;
import org.example.trackit.entity.parts.PartNumber;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CertifiedEquipmentMapper {

    @Mapping(source = "partNumber.partNumber", target = "partNumber")
    CertifiedEquipmentDTO toDTO(CertifiedEquipment certifiedEquipment);

    CertifiedEquipment toEntity(CertifiedEquipmentDTO certifiedEquipmentDTO);


    default PartNumber map(String value) {
        if (value == null) {
            return null; // Handle null case
        }
        PartNumber partNumber = new PartNumber();
        partNumber.setPartNumber(value); // Assuming PartNumber has a setter
        return partNumber;
    }
}
