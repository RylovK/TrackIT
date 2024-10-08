package org.example.trackit.dto;

import jakarta.validation.constraints.NotEmpty;
import org.example.trackit.entity.enums.AllocationStatus;
import org.example.trackit.entity.enums.HealthStatus;
import org.example.trackit.entity.parts.Job;
import org.example.trackit.entity.parts.PartNumber;

import java.time.LocalDateTime;

public class EquipmentDTO {

    @NotEmpty
    private PartNumber partNumber;

    @NotEmpty
    private String serialNumber;

    @NotEmpty
    private HealthStatus healthStatus;

    @NotEmpty
    private AllocationStatus allocationStatus;

    private Job job;

    private LocalDateTime createdAt;

    private LocalDateTime allocationStatusLastModified;
}
