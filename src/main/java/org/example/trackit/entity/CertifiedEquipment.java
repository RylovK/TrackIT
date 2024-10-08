package org.example.trackit.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.trackit.entity.parts.CertificationStatus;
import org.example.trackit.entity.parts.Equipment;
import org.example.trackit.entity.parts.EquipmentType;

import java.time.LocalDate;
import java.time.Period;

@Entity
@Getter
@Setter
public class CertifiedEquipment extends Equipment {

    @Enumerated(EnumType.STRING) //TODO: нужно ли?
    private EquipmentType type;

    @Enumerated(EnumType.STRING)
    private CertificationStatus certificationStatus; // Enum для статусов

    private LocalDate certificationDate;

    private Period certificationPeriod;//in month

    private LocalDate nextCertificationDate;

    private String fileCertificate;

}
