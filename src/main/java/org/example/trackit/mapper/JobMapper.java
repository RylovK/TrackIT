package org.example.trackit.mapper;

import org.example.trackit.dto.JobDTO;
import org.example.trackit.dto.JobResponseDTO;
import org.example.trackit.entity.properties.Job;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = PartNumberMapper.class)
public interface JobMapper {

    JobResponseDTO toResponseDTO(Job job);
    Job toJob(JobResponseDTO jobResponseDTO);

    JobDTO toJobDTO(Job job);
    Job toJob(JobDTO jobDTO);

//    default String map(PartNumber partNumber) {
//        return partNumber != null ? partNumber.getNumber() : null;
//    }
//
//    default PartNumber map(String partNumber) {
//        return partNumber == null ? null : new PartNumber(partNumber);
//    }
}
