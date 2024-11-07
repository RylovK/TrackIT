package org.example.trackit.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class JobResponseDTO {

    private int id;

    @Size(min = 4, max = 25, message = "The job name must be between 4 and 25 symbols")
    @NotEmpty(message = "Job name cannot be empty")
    private String jobName;
}
