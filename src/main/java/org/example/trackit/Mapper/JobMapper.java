package org.example.trackit.Mapper;

import org.example.trackit.dto.JobDTO;
import org.example.trackit.entity.properties.Job;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface JobMapper {

    JobDTO toDTO(Job job);
    Job toJob(JobDTO jobDTO);
}
