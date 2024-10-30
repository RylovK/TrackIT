package org.example.trackit.util;

import jakarta.annotation.PostConstruct;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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
    public void init() {
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
}
