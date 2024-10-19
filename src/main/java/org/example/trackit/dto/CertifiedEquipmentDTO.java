package org.example.trackit.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.trackit.entity.properties.CertificationStatus;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class CertifiedEquipmentDTO extends EquipmentDTO {

    private CertificationStatus certificationStatus;

    private LocalDate certificationDate;

    private int certificationPeriod;//TODO: in month сделать валидацию 6,12,24,36,48,60

    private LocalDate nextCertificationDate;

    private String fileCertificate;

    public CertifiedEquipmentDTO(CreateCertifiedEquipmentDTO createEquipmentDTO) {
        super(createEquipmentDTO);
        this.certificationDate = createEquipmentDTO.getCertificationDate();
        this.certificationPeriod = createEquipmentDTO.getCertificationPeriod();
        this.fileCertificate = createEquipmentDTO.getFileCertificate();
    }
}
