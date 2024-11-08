package org.example.trackit.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.example.trackit.dto.JobDTO;
import org.example.trackit.dto.JobResponseDTO;
import org.example.trackit.exceptions.JobNotFoundException;
import org.example.trackit.exceptions.ValidationErrorException;
import org.example.trackit.services.JobService;
import org.example.trackit.validators.JobValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/job")
@RequiredArgsConstructor
@Tag(name = "Job API", description = "API for job management")
public class JobController {

    private final JobService jobService;
    private final JobValidator jobValidator;

    @GetMapping
    @Operation(summary = "Get list of all job")
    public ResponseEntity<List<JobResponseDTO>> getAllJobs() {
        return new ResponseEntity<>(jobService.findAllJobs(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get job by id", description = "Returning job with all equipment list")
    public ResponseEntity<JobDTO> getJobById(@PathVariable int id) {
        return new ResponseEntity<>(jobService.findJobById(id), HttpStatus.OK);
    }

    @PostMapping
    @Operation(summary = "Create new job")
    public ResponseEntity<JobDTO> createJob(@RequestBody @Valid JobResponseDTO jobResponseDTO) {
        JobDTO dto = new JobDTO(jobResponseDTO.getJobName());
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(dto, "jobDTO");
        jobValidator.validate(dto, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new ValidationErrorException(bindingResult);
        }
        JobDTO saved = jobService.save(dto);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<JobDTO> updateJob(@PathVariable int id, @RequestBody @Valid JobResponseDTO jobResponseDTO) {
        JobDTO updated = new JobDTO(jobResponseDTO.getJobName());
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(updated, "jobDTO");
        jobValidator.validate(updated, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new ValidationErrorException(bindingResult);
        }
        JobDTO saved = jobService.update(id, updated);
        return new ResponseEntity<>(saved, HttpStatus.OK);
    }


    @DeleteMapping("/{id}")
    @Operation(summary = "Delete job")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteJob(@PathVariable int id) {
        if (jobService.delete(id)){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        throw new JobNotFoundException("Job not found: " + id);
    }
}
