package org.example.trackit.util;

import jakarta.annotation.PostConstruct;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.trackit.dto.CertifiedEquipmentDTO;
import org.example.trackit.dto.EquipmentDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class FileUtils {

    private final String imagesDirectory;
    private final String certificatesDirectory;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public FileUtils(@Value("${file.images.directory}") String imagesDirectory,
                     @Value("${file.certificates.directory}") String certificatesDirectory) {
        this.imagesDirectory = imagesDirectory;
        this.certificatesDirectory = certificatesDirectory;
    }

    @PostConstruct
    private void init() {
        File uploadDirectory = new File(imagesDirectory);
        File uploadCertificateDirectory = new File(certificatesDirectory);
        if (!uploadDirectory.exists()) {
            uploadDirectory.mkdirs();
        }
        if (!uploadCertificateDirectory.exists()) {
            uploadCertificateDirectory.mkdirs();
        }
    }

    public String savePhoto(String name, MultipartFile file) {
        try {
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null || !originalFilename.contains(".")) {
                throw new IllegalArgumentException("Uploaded file must have an extension.");
            }
            String extension = FilenameUtils.getExtension(originalFilename);
            String formattedDate = LocalDate.now().format(formatter);
            String fileName = String.format("%s_%s.%s", name, formattedDate, extension);

            Path filePath = Paths.get(imagesDirectory, fileName);

            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (IOException e) {
            throw new RuntimeException("Failed to save file", e);
        }
    }

    public String saveCertificate(MultipartFile file) {
        try {
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null || !originalFilename.contains(".")) {
                throw new IllegalArgumentException("Uploaded file must have an extension.");
            }
            String fileName = generateFileName(originalFilename);
            Path filePath = Paths.get(certificatesDirectory, fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String generateFileName(String originalFilename) {
        String fileNameWithOutExt = FilenameUtils.removeExtension(originalFilename);
        String extension = FilenameUtils.getExtension(originalFilename);
        String formattedDate = LocalDate.now().format(formatter);
        return String.format("%s_%s.%s", fileNameWithOutExt, formattedDate, extension);
    }

    public <T extends EquipmentDTO> ByteArrayInputStream getInputStream(List<T> equipmentList, Class<T> clazz) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Equipment");
        Row headerRow = sheet.createRow(0);
        int rowNum = 1;
        if (clazz == EquipmentDTO.class) {
            fillHeaders(headerRow);
            for (T equipment : equipmentList) {
                Row row = sheet.createRow(rowNum++);
                fillDataToRow(row, equipment);
            }
        } else {
            fillCertifiedHeaders(headerRow);
            for (T equipment : equipmentList) {
                Row row = sheet.createRow(rowNum++);
                fillCertifiedDataToRow(row, (CertifiedEquipmentDTO) equipment);
            }
        }
        try {
            workbook.write(outputStream);
            workbook.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new ByteArrayInputStream(outputStream.toByteArray());
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
