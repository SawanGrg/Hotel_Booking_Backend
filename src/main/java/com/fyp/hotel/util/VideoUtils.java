package com.fyp.hotel.util;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class VideoUtils {

    // Directory to save the chunks
    String saveDirectory = "D:/projects/";

    // List to hold the file chunks
    List<byte[]> chunks = new ArrayList<>();

    public void processFile(MultipartFile file) {
        try {
            InputStream inputStream = file.getInputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {

                //arrays.copyOf() method is used to copy the specified array, truncating or padding with zeros (if necessary) so the copy has the specified length.
                //buffer is the array to be copied to the new array
                //bytesRead is the length of the copy to be returned
                byte[] chunk = Arrays.copyOf(buffer, bytesRead);
                chunks.add(chunk);
            }
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        byte[] mergedBytes = mergeChunks(chunks);
        String mergedFilePath = saveDirectory + "merged_file.mp4";
        saveByteArrayToFile(mergedBytes, mergedFilePath);
        System.out.println("Merged file saved to: " + mergedFilePath);
    }

    // Method to merge all chunks into a single byte array
    private static byte[] mergeChunks(List<byte[]> chunks) {
        int totalSize = chunks.stream()
                .mapToInt(chunk -> chunk.length)
                .sum();
        byte[] mergedBytes = new byte[totalSize];
        int position = 0;
        for (byte[] chunk : chunks) {
            System.arraycopy(chunk, 0, mergedBytes, position, chunk.length);
            position += chunk.length;
        }
        return mergedBytes;
    }

    // Method to save a byte array to a file
    private static void saveByteArrayToFile(byte[] bytes, String filePath) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(filePath)) {
            fileOutputStream.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to load video content into a byte array
    public byte[] loadVideoContent() {
        try {
            InputStream inputStream = new FileInputStream("D:/projects/merged_file.mp4");
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            return outputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return new byte[0];
        }
    }
}
