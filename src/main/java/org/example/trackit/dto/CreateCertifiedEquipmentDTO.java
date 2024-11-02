package org.example.trackit.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CreateCertifiedEquipmentDTO extends CreateEquipmentDTO {

    private LocalDate certificationDate;

    private int certificationPeriod;//TODO: in month сделать валидацию 6,12,24,36,48,60

    private String fileCertificate;

    public CreateCertifiedEquipmentDTO() {
        certificationDate = LocalDate.now();
    }
}
