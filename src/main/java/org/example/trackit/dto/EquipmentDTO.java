package org.example.trackit.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.example.trackit.entity.properties.AllocationStatus;
import org.example.trackit.entity.properties.HealthStatus;
import org.example.trackit.entity.properties.Job;

import java.time.LocalDateTime;

@Setter
@Getter
public class EquipmentDTO {

    private int id;

    @NotEmpty
    private String partNumber;

    @NotEmpty
    private String serialNumber;

    private HealthStatus healthStatus;

    private AllocationStatus allocationStatus;

    private Job job;

    private LocalDateTime createdAt;

    private LocalDateTime allocationStatusLastModified;
}
