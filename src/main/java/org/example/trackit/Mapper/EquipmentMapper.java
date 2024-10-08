package org.example.trackit.Mapper;
import org.example.trackit.dto.EquipmentDTO;
import org.example.trackit.entity.parts.Equipment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
@Mapper(componentModel = "spring")
public interface EquipmentMapper {


        @Mapping(source = "partNumber.partNumber", target = "partNumber")
        EquipmentDTO toDTO(Equipment equipment);

        Equipment toEntity(EquipmentDTO equipmentDTO);

}
