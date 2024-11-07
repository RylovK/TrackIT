package org.example.trackit.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class JobDTO {

    private int id;

    @NotEmpty(message = "Job name cannot be empty")
    @Size(min = 4, max = 25, message = "The job name must be between 4 and 25 symbols")
    private String jobName;

    private List<EquipmentDTO> equipment;

    public JobDTO(String jobName) {
        this.jobName = jobName;
        equipment = new ArrayList<>();
    }
}
