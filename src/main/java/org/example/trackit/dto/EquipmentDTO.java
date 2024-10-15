package org.example.trackit.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.example.trackit.entity.properties.AllocationStatus;
import org.example.trackit.entity.properties.HealthStatus;

import java.time.LocalDateTime;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EquipmentDTO {

    private int id;

    @JsonIgnore
    private PartNumberDTO partNumberDTO;

    private String serialNumber;

    @JsonIgnore
    private JobResponseDTO jobResponseDTO;

    private HealthStatus healthStatus;

    private AllocationStatus allocationStatus;

    private LocalDateTime createdAt;

    private LocalDateTime allocationStatusLastModified;

    public EquipmentDTO() {
        partNumberDTO = new PartNumberDTO();
        jobResponseDTO = new JobResponseDTO();
    }

    public EquipmentDTO(CreateEquipmentDTO createEquipmentDTO) {
        partNumberDTO = new PartNumberDTO();
        partNumberDTO.setNumber(createEquipmentDTO.getPartNumber());
        jobResponseDTO = new JobResponseDTO();
        this.serialNumber = createEquipmentDTO.getSerialNumber();
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
        return jobResponseDTO.getJobName();
    }

    public void setJobName(String job) {
        this.jobResponseDTO.setJobName(job);
    }

}
