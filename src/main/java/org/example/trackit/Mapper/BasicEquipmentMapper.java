package org.example.trackit.Mapper;

import org.example.trackit.dto.BasicEquipmentDTO;
import org.example.trackit.entity.BasicEquipment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BasicEquipmentMapper {

    @Mapping(source = "partNumber.partNumber", target = "partNumber")
    BasicEquipmentDTO toDTO(BasicEquipment basicEquipment);

    BasicEquipment toEntity(BasicEquipmentDTO basicEquipmentDTO);
}
