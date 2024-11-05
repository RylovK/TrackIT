package org.example.trackit.Mapper;

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
        if (partNumber == null) {
            return null;
        }
        PartNumber result =  new PartNumber();
        result.setNumber(partNumber);
        return result;
    }
}
