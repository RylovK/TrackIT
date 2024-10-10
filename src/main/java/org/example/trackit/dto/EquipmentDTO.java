package org.example.trackit.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.example.trackit.entity.Equipment;
import org.example.trackit.entity.properties.AllocationStatus;
import org.example.trackit.entity.properties.HealthStatus;
import org.example.trackit.entity.properties.Job;

import java.time.LocalDateTime;
import java.util.Set;

@Setter
@Getter
public class EquipmentDTO {

    private int id;

    @JsonIgnore
    private PartNumberDTO partNumberDTO;

    @NotEmpty
    private String serialNumber;

    private HealthStatus healthStatus;

    private AllocationStatus allocationStatus;

    private Job job;

    private LocalDateTime createdAt;

    private LocalDateTime allocationStatusLastModified;

    public String getPartNumber() {
        return partNumberDTO.getNumber();
    }

    public EquipmentDTO() {
        partNumberDTO = new PartNumberDTO();
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

    public void setPhoto(String photo) {
        partNumberDTO.setPhoto(photo);
    }
}
