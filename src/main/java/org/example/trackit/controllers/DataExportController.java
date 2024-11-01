package org.example.trackit.controllers;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Workbook;
import org.example.trackit.dto.CertifiedEquipmentDTO;
import org.example.trackit.dto.EquipmentDTO;
import org.example.trackit.services.EquipmentService;
import org.example.trackit.util.FileUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/export")
@RequiredArgsConstructor
public class DataExportController {

    private final EquipmentService<EquipmentDTO> equipmentService;
    private final EquipmentService<CertifiedEquipmentDTO> certifiedEquipmentService;
    private final FileUtils fileUtils;

    @GetMapping("/all")
    public ResponseEntity<byte[]> exportAll() {
        List<EquipmentDTO> equipmentList = equipmentService.findAll();
        ByteArrayInputStream inputStream = fileUtils.getInputStream(equipmentList, EquipmentDTO.class);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=equipment.xlsx");
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(inputStream.readAllBytes());
    }

    @GetMapping("/certified")
    public ResponseEntity<byte[]> exportCertified() {
        List<CertifiedEquipmentDTO> certifiedList = certifiedEquipmentService.findAll();
        ByteArrayInputStream inputStream = fileUtils.getInputStream(certifiedList, CertifiedEquipmentDTO.class);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=certified.xlsx");
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(inputStream.readAllBytes());
    }
}
