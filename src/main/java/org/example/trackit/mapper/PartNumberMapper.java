package org.example.trackit.mapper;

import org.example.trackit.dto.PartNumberDTO;
import org.example.trackit.entity.properties.PartNumber;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PartNumberMapper {

    PartNumberDTO toDTO(PartNumber partNumber);
    PartNumber toEntity(PartNumberDTO partNumberDTO);

    default String toString(PartNumber partNumber) {
        return partNumber == null ? null : partNumber.getNumber();
    }

    default PartNumber map(String partNumber) {
        return partNumber == null ? null : new PartNumber(partNumber);
    }
}
