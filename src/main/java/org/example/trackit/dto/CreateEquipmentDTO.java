package org.example.trackit.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateEquipmentDTO {

    @NotEmpty
    private String serialNumber;

    @NotEmpty
    private String partNumber;
}
