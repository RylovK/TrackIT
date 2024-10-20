package org.example.trackit.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.trackit.entity.Equipment;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class JobDTO {

    private int id;

    @NotEmpty
    private String jobName;

    private Set<Equipment> equipment;

    public JobDTO(String jobName) {
        this.jobName = jobName;
        equipment = new HashSet<>();
    }
}
