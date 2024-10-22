package org.example.trackit.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class JobResponseDTO {

    @Size(min = 4, max = 25, message = "The job name must be between 4 and 25 symbols")
    private String jobName;
}
