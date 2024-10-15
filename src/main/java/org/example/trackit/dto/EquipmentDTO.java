package org.example.trackit.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.example.trackit.entity.properties.AllocationStatus;
import org.example.trackit.entity.properties.HealthStatus;

import java.time.LocalDateTime;

@Setter
@Getter
public class EquipmentDTO {

    private int id;

    @JsonIgnore
    private PartNumberDTO partNumberDTO;

    @JsonIgnore
    private JobDTO jobDTO;

    private String serialNumber;

    private HealthStatus healthStatus;

    private AllocationStatus allocationStatus;

    private LocalDateTime createdAt;

    private LocalDateTime allocationStatusLastModified;

    public EquipmentDTO() {
        partNumberDTO = new PartNumberDTO();
        jobDTO = new JobDTO();
    }

    public String getPartNumber() {
        return partNumberDTO.getNumber();
    }

    public void setPartNumber(String partNumber) {
        this.partNumberDTO.setNumber(partNumber);
    }

    public String getDescription() {
        return partNumberDTO.getDescription();
    }

    public String getPhoto() {
        return partNumberDTO.getPhoto();
    }

    public String getJobName() {
        return jobDTO.getJobName();
    }

    public void setJobName(String job) {
        this.jobDTO.setJobName(job);
    }

}
