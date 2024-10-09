package org.example.trackit.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.trackit.entity.properties.CertificationStatus;

import java.time.LocalDate;
import java.time.Period;

@Entity
@Getter
@Setter
public class CertifiedEquipment extends Equipment {

    @Enumerated(EnumType.STRING)
    private CertificationStatus certificationStatus = CertificationStatus.EXPIRED;

    private LocalDate certificationDate;

    private Period certificationPeriod = Period.ofMonths(12);//in month

    private LocalDate nextCertificationDate;

    private String fileCertificate;

}
