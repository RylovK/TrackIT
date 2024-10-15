package org.example.trackit.Mapper;

import org.example.trackit.dto.JobDTO;
import org.example.trackit.dto.JobResponseDTO;
import org.example.trackit.entity.properties.Job;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface JobMapper {

    JobResponseDTO toResponseDTO(Job job);
    Job toJob(JobResponseDTO jobResponseDTO);

    JobDTO toJobDTO(Job job);
    Job toJob(JobDTO jobDTO);
}
