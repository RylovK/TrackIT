package org.example.trackit.controllers;

import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.trackit.dto.CertifiedEquipmentDTO;
import org.example.trackit.dto.EquipmentDTO;
import org.example.trackit.services.EquipmentService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/export")
@AllArgsConstructor
public class DataExportController {

    private final EquipmentService<EquipmentDTO> equipmentService;
    private final EquipmentService<CertifiedEquipmentDTO> certifiedEquipmentService;

    @GetMapping("/all")
    public ResponseEntity<byte[]> exportAll() throws IOException {
        List<EquipmentDTO> equipmentList = equipmentService.findAll();
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Equipment");
            Row headerRow = sheet.createRow(0);
            fillHeaders(headerRow);
            int rowNum = 1;
            for (EquipmentDTO dto : equipmentList) {
                Row row = sheet.createRow(rowNum++);
                fillDataToRow(row, dto);
            }
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=equipment.xlsx");
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(inputStream.readAllBytes());
        }
    }
    @GetMapping("/certified")
    public ResponseEntity<byte[]> exportCertified() throws IOException {
        List<CertifiedEquipmentDTO> certifiedList = certifiedEquipmentService.findAll();
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Certified Equipment");
            Row headerRow = sheet.createRow(0);
            fillCertifiedHeaders(headerRow);
            int rowNum = 1;
            for (CertifiedEquipmentDTO dto : certifiedList) {
                Row row = sheet.createRow(rowNum++);
                fillCertifiedDataToRow(row, dto);
            }
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=certified.xlsx");
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(inputStream.readAllBytes());
        }
    }

    private void fillCertifiedDataToRow(Row row, CertifiedEquipmentDTO dto) {
        fillDataToRow(row, dto);
        row.createCell(10).setCellValue(dto.getCertificationStatus().toString());
        row.createCell(11).setCellValue(dto.getCertificationDate().toString());
        row.createCell(12).setCellValue(dto.getCertificationPeriod());
        row.createCell(13).setCellValue(dto.getNextCertificationDate().toString());
    }

    private void fillCertifiedHeaders(Row headerRow) {
        fillHeaders(headerRow);
        headerRow.createCell(10).setCellValue("Certification status");
        headerRow.createCell(11).setCellValue("Certification date");
        headerRow.createCell(12).setCellValue("Certification period");
        headerRow.createCell(13).setCellValue("Next certification date");
    }

    private void fillDataToRow(Row row, EquipmentDTO dto) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        row.createCell(0).setCellValue(dto.getId());
        row.createCell(1).setCellValue(dto.getSerialNumber());
        row.createCell(2).setCellValue(dto.getPartNumber());
        row.createCell(3).setCellValue(dto.getDescription());
        row.createCell(4).setCellValue(dto.getHealthStatus().toString());
        row.createCell(5).setCellValue(dto.getAllocationStatus().toString());
        row.createCell(6).setCellValue(dto.getJobName());
        row.createCell(7).setCellValue(dto.getLastJob());
        row.createCell(8).setCellValue(dto.getAllocationStatusLastModified() == null ? null : dto.getAllocationStatusLastModified().toString());
        row.createCell(9).setCellValue(dto.getComments());
        row.createCell(10).setCellValue(dto.getCreatedAt().format(formatter));
    }

    private void fillHeaders(Row headerRow) {
        headerRow.createCell(0).setCellValue("ID");
        headerRow.createCell(1).setCellValue("Serial Number");
        headerRow.createCell(2).setCellValue("Part Number");
        headerRow.createCell(3).setCellValue("Description");
        headerRow.createCell(4).setCellValue("Health Status");
        headerRow.createCell(5).setCellValue("Allocation Status");
        headerRow.createCell(6).setCellValue("Job name");
        headerRow.createCell(7).setCellValue("Last job name");
        headerRow.createCell(8).setCellValue("Allocation Status Last Modified");
        headerRow.createCell(9).setCellValue("Comments");
        headerRow.createCell(10).setCellValue("Created At");
    }
}
