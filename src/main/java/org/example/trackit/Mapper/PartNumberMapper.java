package org.example.trackit.Mapper;

import org.example.trackit.dto.PartNumberDTO;
import org.example.trackit.entity.properties.PartNumber;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PartNumberMapper {

    PartNumberDTO toDTO(PartNumber partNumber);
    PartNumber toEntity(PartNumberDTO partNumberDTO);
}
