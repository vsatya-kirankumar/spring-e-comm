package com.ecommerce.project.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

public class Utils {
    public static String uploadImage(String pathName, MultipartFile file) throws IOException {
        String originalFileName = file.getOriginalFilename();
        assert originalFileName != null;

        String fileType = originalFileName.substring(originalFileName.lastIndexOf("."));
        String uuid = UUID.randomUUID().toString();
        String fileName = uuid.concat(fileType);
        String filePath = pathName + File.separator + fileName;

        File newFile = new File(pathName);
        if(!newFile.exists()){
            newFile.mkdir();
        }

        Files.copy(file.getInputStream(), Paths.get(filePath));

        return fileName;
    }
}