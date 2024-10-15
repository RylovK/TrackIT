package org.example.trackit.validators;

import lombok.AllArgsConstructor;
import org.example.trackit.dto.JobDTO;
import org.example.trackit.entity.properties.Job;
import org.example.trackit.repository.JobRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

@Component
@AllArgsConstructor
public class JobValidator implements Validator {

    private final JobRepository jobRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return Job.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        JobDTO job = (JobDTO) target;
        Optional<Job> founded = jobRepository.findByJobName(job.getJobName());
        if (founded.isPresent()) {
            errors.rejectValue("jobName", "duplicate", "Job already exists");
        }
    }
}
