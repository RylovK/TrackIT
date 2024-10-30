package org.example.trackit.services.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.trackit.entity.CertifiedEquipment;
import org.example.trackit.entity.properties.PartNumber;
import org.example.trackit.exceptions.PartNumberNotFoundException;
import org.example.trackit.repository.CertifiedEquipmentRepository;
import org.example.trackit.repository.PartNumberRepository;
import org.example.trackit.util.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class FileService {

    private final CertifiedEquipmentRepository certifiedEquipmentRepository;
    private final PartNumberRepository partNumberRepository;
    private final FileUtils fileUtils;

    @Transactional
    public String saveFile(int id, MultipartFile file) {
        CertifiedEquipment found = certifiedEquipmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Equipment not found"));
        String filePath = fileUtils.saveCertificate(file);
        found.setFileCertificate(filePath);
        certifiedEquipmentRepository.save(found);
        return filePath;
    }

    @Transactional
    public String saveImage(String partNumber, MultipartFile file) {
        PartNumber entity = partNumberRepository.findByNumber(partNumber)
                .orElseThrow(() -> new PartNumberNotFoundException("Part number not found"));

        String filePath = fileUtils.savePhoto(partNumber, file);
        entity.setPhoto(filePath);
        partNumberRepository.save(entity);

        return filePath;
    }
}
