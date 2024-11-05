package org.example.trackit.services;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Transactional
public interface FileService {

    String saveFile(int id, MultipartFile file);

    String saveImage(String partNumber, MultipartFile file);
}
