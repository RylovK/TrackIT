package org.example.trackit.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.trackit.entity.enums.CertificationStatus;
import org.example.trackit.entity.parts.Equipment;

import java.time.LocalDate;
import java.time.Period;

@Entity
@Getter
@Setter
public class CertifiedEquipment extends Equipment {

    @Enumerated(EnumType.STRING)
    private CertificationStatus certificationStatus;

    private LocalDate certificationDate;

    private Period certificationPeriod;//in month

    private LocalDate nextCertificationDate;

    private String fileCertificate;

}
