package org.example.trackit.Mapper;
import org.example.trackit.dto.EquipmentDTO;
import org.example.trackit.entity.parts.Equipment;
import org.example.trackit.entity.parts.PartNumber;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
@Mapper(componentModel = "spring")
public interface EquipmentMapper {


        @Mapping(source = "partNumber.partNumber", target = "partNumber")
        EquipmentDTO toDTO(Equipment equipment);

        Equipment toEntity(EquipmentDTO equipmentDTO);

        default PartNumber map(String value) {
                if (value == null) {
                        return null; // Handle null case
                }
                PartNumber partNumber = new PartNumber();
                partNumber.setPartNumber(value); // Assuming PartNumber has a setter
                return partNumber;
        }
}
