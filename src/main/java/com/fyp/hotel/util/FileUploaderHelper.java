package com.fyp.hotel.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Component
public class FileUploaderHelper {

    private static final Logger logger = LoggerFactory.getLogger(FileUploaderHelper.class);

    private final String pathToSave;

    public FileUploaderHelper() {
        // Make the directory path configurable or provide a default path
        this.pathToSave = "src/main/resources/static/images";
        // Create directory if it doesn't exist
        createDirectoryIfNotExists(pathToSave);
        logger.info("File Upload Helper Constructor called");
    }

    /**
     * Uploads a file to the specified directory.
     *
     * @param file The MultipartFile to be uploaded.
     * @return True if the file was uploaded successfully, false otherwise.
     */
    public boolean fileUploader(MultipartFile file) {
        if (file.isEmpty()) {
            logger.error("Cannot upload empty file");
            return false;
        }

        try {
            Path destination = Paths.get(pathToSave + File.separator + file.getOriginalFilename());
            Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
            logger.info("File uploaded successfully: {}", destination);
            return true;
        } catch (IOException e) {
            logger.error("Failed to upload file: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Creates a directory if it doesn't exist.
     *
     * @param directoryPath The path of the directory to be created.
     */
    private void createDirectoryIfNotExists(String directoryPath) {
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            if (directory.mkdirs()) {
                logger.info("Directory created: {}", directoryPath);
            } else {
                logger.error("Failed to create directory: {}", directoryPath);
            }
        }
    }
}
