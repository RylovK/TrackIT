package org.example.trackit.Mapper;

import org.example.trackit.dto.CertifiedEquipmentDTO;
import org.example.trackit.entity.CertifiedEquipment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CertifiedEquipmentMapper {

    @Mapping(source = "partNumber.partNumber", target = "partNumber")
    CertifiedEquipmentDTO toDTO(CertifiedEquipment certifiedEquipment);

    CertifiedEquipment toEntity(CertifiedEquipmentDTO certifiedEquipmentDTO);
}
