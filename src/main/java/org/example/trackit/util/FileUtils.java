package org.example.trackit.util;

import jakarta.annotation.PostConstruct;
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
import java.util.UUID;

@Service
public class FileUtils {

    private final String imagesDirectory;
    private final String certificatesDirectory;

    public FileUtils(@Value("${file.images.directory}") String imagesDirectory,
                     @Value("${file.certificates.directory}") String certificatesDirectory) {
        this.imagesDirectory = imagesDirectory;
        this.certificatesDirectory = certificatesDirectory;
    }

    @PostConstruct
    public void init() {
        File uploadDirectory = new File(imagesDirectory);
        if (!uploadDirectory.exists()) {
            uploadDirectory.mkdirs();
        }
    }

    public String savePhoto(String name, MultipartFile file) {
        try {
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null || !originalFilename.contains(".")) {
                throw new IllegalArgumentException("Uploaded file must have an extension.");
            }
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String fileName = name + extension;
            Path filePath = Paths.get(imagesDirectory, fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            return filePath.toString();
        } catch (IOException e) {
            throw new RuntimeException("Failed to save file", e);
        }
    }

    public String saveCertificate(MultipartFile file) {
        try {
            String originalFilename = file.getOriginalFilename();
            String fileName = originalFilename + "_" + LocalDate.now();
            Path filePath = Paths.get(certificatesDirectory, fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            return filePath.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
