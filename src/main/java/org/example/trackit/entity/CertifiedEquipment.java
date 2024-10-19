package org.example.trackit.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.trackit.entity.properties.CertificationStatus;
import org.example.trackit.entity.properties.PartNumber;

import java.time.LocalDate;
import java.time.Period;

@Entity
@Getter
@Setter
public class CertifiedEquipment extends Equipment {

    @Transient
    @Setter(AccessLevel.NONE)
    private CertificationStatus certificationStatus;

    private LocalDate certificationDate;

    private int certificationPeriod;//in month 6.12.24.36.48.60

    private LocalDate nextCertificationDate;

    private String fileCertificate;

    public CertifiedEquipment() {
        super();
        certificationStatus = CertificationStatus.EXPIRED;
        certificationPeriod = 12;
    }

    public CertifiedEquipment(PartNumber partNumber, String serialNumber) {
        super(partNumber, serialNumber);
        certificationStatus = CertificationStatus.EXPIRED;
        certificationPeriod = 12;
    }

    public CertificationStatus getCertificationStatus() {
        LocalDate now = LocalDate.now();
        if (certificationDate != null) {
            if (now.isBefore(certificationDate.plusMonths(certificationPeriod))) {
                return CertificationStatus.VALID;
            }
        }
        return CertificationStatus.EXPIRED;
    }
}
