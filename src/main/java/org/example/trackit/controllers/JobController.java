package org.example.trackit.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.example.trackit.dto.JobDTO;
import org.example.trackit.exceptions.JobAlreadyExistException;
import org.example.trackit.exceptions.JobNotFoundException;
import org.example.trackit.exceptions.ValidationErrorException;
import org.example.trackit.services.JobService;
import org.example.trackit.validators.JobValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/job")
@AllArgsConstructor
@Tag(name = "Job API", description = "API for job management")
public class JobController {

    private final JobService jobService;
    private final JobValidator jobValidator;

    @GetMapping
    @Operation(summary = "Get list of all job")
    public ResponseEntity<List<JobDTO>> getAllJobs() {
        return new ResponseEntity<>(jobService.findAllJobs(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get job by id", description = "Returning job with all equipment list")
    public ResponseEntity<JobDTO> getJobById(@PathVariable int id) {
        return new ResponseEntity<>(jobService.findJobById(id), HttpStatus.OK);
    }

    @PostMapping
    @Operation(summary = "Create new job")
    public ResponseEntity<JobDTO> createJob(@RequestParam String jobName) {
        JobDTO dto = new JobDTO(jobName);
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(dto, "jobDTO");
        jobValidator.validate(dto, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new ValidationErrorException(bindingResult);
        }
        JobDTO saved = jobService.save(dto);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<JobDTO> updateJob(@PathVariable int id, @RequestParam String jobName) {
        JobDTO updated = new JobDTO(jobName);
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(updated, "jobDTO");
        jobValidator.validate(updated, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new ValidationErrorException(bindingResult);
        }
        JobDTO saved = jobService.update(id, updated);
        return new ResponseEntity<>(saved, HttpStatus.OK);
    }


    @DeleteMapping
    @Operation(summary = "Delete job")
    public ResponseEntity<Void> deleteJob(@RequestParam int id) {
        if (jobService.delete(id)){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        throw new JobNotFoundException("Job not found: " + id);
    }
}
