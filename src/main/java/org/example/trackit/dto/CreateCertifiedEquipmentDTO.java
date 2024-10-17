package org.example.trackit.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.Period;

@Getter
@Setter
public class CreateCertifiedEquipmentDTO extends CreateEquipmentDTO {

    private LocalDate certificationDate;

    @JsonIgnore
    private Period certificationPeriod;//TODO: in month сделать валидацию 6,12,24,36,48,60

    private String fileCertificate;

    public int getPeriod() {
        return certificationPeriod.getMonths();
    }
    public void setPeriod(int months) {
        certificationPeriod = Period.ofMonths(months);
    }
}
