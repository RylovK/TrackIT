package org.example.trackit.Mapper;

import org.example.trackit.dto.BasicEquipmentDTO;
import org.example.trackit.entity.BasicEquipment;
import org.example.trackit.entity.parts.PartNumber;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BasicEquipmentMapper {

    @Mapping(source = "partNumber.partNumber", target = "partNumber")
    BasicEquipmentDTO toDTO(BasicEquipment basicEquipment);

    BasicEquipment toEntity(BasicEquipmentDTO basicEquipmentDTO);

    default PartNumber map(String value) {
        if (value == null) {
            return null; // Handle null case
        }
        PartNumber partNumber = new PartNumber();
        partNumber.setPartNumber(value); // Assuming PartNumber has a setter
        return partNumber;
    }
}
