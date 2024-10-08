package org.example.trackit.dto;

import org.example.trackit.entity.enums.CertificationStatus;

import java.time.LocalDate;
import java.time.Period;

public class CertifiedEquipmentDTO extends EquipmentDTO {

    private CertificationStatus certificationStatus; // Enum для статусов

    private LocalDate certificationDate;

    private Period certificationPeriod;//TODO: in month сделать валидацию 6,12,24,36,48,60

    private LocalDate nextCertificationDate;

    private String fileCertificate;
}
