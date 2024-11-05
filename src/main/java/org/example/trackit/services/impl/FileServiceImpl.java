package org.example.trackit.services.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.trackit.entity.CertifiedEquipment;
import org.example.trackit.entity.properties.PartNumber;
import org.example.trackit.exceptions.PartNumberNotFoundException;
import org.example.trackit.repository.CertifiedEquipmentRepository;
import org.example.trackit.repository.PartNumberRepository;
import org.example.trackit.services.FileService;
import org.example.trackit.util.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class FileServiceImpl implements FileService {

    private final CertifiedEquipmentRepository certifiedEquipmentRepository;
    private final PartNumberRepository partNumberRepository;
    private final FileUtils fileUtils;

    @Override
    public String saveFile(int id, MultipartFile file) {
        CertifiedEquipment found = certifiedEquipmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Equipment not found"));
        String fileName = fileUtils.saveCertificate(file);
        found.setFileCertificate(fileName);
        certifiedEquipmentRepository.save(found);
        log.info("Saved file {} for {}: {}", fileName, found.getPartNumber().getNumber(), found.getSerialNumber());
        return fileName;
    }

    @Override
    public String saveImage(String partNumber, MultipartFile file) {
        PartNumber entity = partNumberRepository.findByNumber(partNumber)
                .orElseThrow(() -> new PartNumberNotFoundException("Part number not found"));

        String fileName = fileUtils.savePhoto(partNumber, file);
        entity.setPhoto(fileName);
        partNumberRepository.save(entity);
        log.info("Saved image {} for {}", fileName, entity.getNumber());
        return fileName;
    }
}
