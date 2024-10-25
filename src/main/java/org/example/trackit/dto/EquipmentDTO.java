package org.example.trackit.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.example.trackit.entity.properties.AllocationStatus;
import org.example.trackit.entity.properties.HealthStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EquipmentDTO {

    private int id;

    @JsonIgnore
    private PartNumberDTO partNumberDTO;

    @NotEmpty
    private String serialNumber;

    private HealthStatus healthStatus;

    private AllocationStatus allocationStatus;

    @JsonIgnore
    private JobResponseDTO jobResponseDTO;

    private String lastJob;

    private LocalDate allocationStatusLastModified;

    private String comments;

    private LocalDateTime createdAt;


    public EquipmentDTO() {
        partNumberDTO = new PartNumberDTO();
        jobResponseDTO = new JobResponseDTO();
    }

    public EquipmentDTO(CreateEquipmentDTO createEquipmentDTO) {
        this.partNumberDTO = new PartNumberDTO();
        this.partNumberDTO.setNumber(createEquipmentDTO.getPartNumber());
        this.jobResponseDTO = new JobResponseDTO();
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

    public void setDescription(String description) {
        this.partNumberDTO.setDescription(description);
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
