package com.fyp.hotel.util;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Component
public class FileUploaderHelper {

    // The directory path where uploaded files will be saved
    private final String pathToSave = "src/main/resources/static/images";

    // Constructor for FileUploaderHelper
    public FileUploaderHelper() throws IOException {
        System.out.println("File Upload Helper Constructor called");
    }

    /**
     * Uploads a file to the specified directory.
     *
     * @param file The MultipartFile to be uploaded.
     * @return True if the file was uploaded successfully, false otherwise.
     */
    public boolean fileUploader(MultipartFile file) {
        boolean state = false;
        try {
            // Copy the contents of the uploaded file's InputStream to the destination directory
            //file.getInputStream() returns an InputStream object which points to the uploaded file
            //which means that we can read the contents of the uploaded file from this InputStream object
            Files.copy(file.getInputStream(), Paths.get(pathToSave + File.separator + file.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);
            
            // Set the state to true to indicate successful upload
            state = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return state;
    }
}