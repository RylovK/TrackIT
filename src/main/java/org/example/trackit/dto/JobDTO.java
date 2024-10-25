package org.example.trackit.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class JobDTO {

    private int id;

    @Size(min = 4, max = 25, message = "The job name must be between 4 and 25 symbols")
    private String jobName;

    private Set<EquipmentDTO> equipment;

    public JobDTO(String jobName) {
        this.jobName = jobName;
        equipment = new HashSet<>();
    }
}
