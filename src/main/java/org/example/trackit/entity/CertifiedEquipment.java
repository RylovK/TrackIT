package org.example.trackit.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.trackit.entity.properties.CertificationStatus;
import org.example.trackit.entity.properties.PartNumber;

import java.time.LocalDate;
import java.time.Period;

@Entity
@Getter
public class CertifiedEquipment extends Equipment {

    @Enumerated(EnumType.STRING)
    private CertificationStatus certificationStatus; //TODO:автоматическая установка

    private LocalDate certificationDate;

    private Period certificationPeriod;//in month 6.12.24.36.48.60

    private LocalDate nextCertificationDate; //TODO:автоматическая установка

    private String fileCertificate;

    public CertifiedEquipment(PartNumber partNumber, String serialNumber, LocalDate certificationDate, Period certificationPeriod, String fileCertificate) {
        super(partNumber, serialNumber);
        this.certificationDate = certificationDate;
        this.certificationPeriod = certificationPeriod;
        this.nextCertificationDate = certificationDate.plus(certificationPeriod);
        if (nextCertificationDate.isBefore(LocalDate.now())) {
            certificationStatus = CertificationStatus.EXPIRED;
        } else {
            certificationStatus = CertificationStatus.VALID;
        }
        this.fileCertificate = fileCertificate;
    }

    public CertifiedEquipment() {
        super();
        certificationStatus = CertificationStatus.EXPIRED;
        certificationPeriod = Period.ofMonths(12);
    }

    public CertifiedEquipment(PartNumber partNumber, String serialNumber) {
        super(partNumber, serialNumber);
        certificationStatus = CertificationStatus.EXPIRED;
        certificationPeriod = Period.ofMonths(12);
    }
}
