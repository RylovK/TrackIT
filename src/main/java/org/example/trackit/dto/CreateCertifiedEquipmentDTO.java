package org.example.trackit.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CreateCertifiedEquipmentDTO extends CreateEquipmentDTO {

    private LocalDate certificationDate;

    private int certificationPeriod;

    private String fileCertificate;

    public CreateCertifiedEquipmentDTO() {
        certificationDate = LocalDate.now();
    }
}
