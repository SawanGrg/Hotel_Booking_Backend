//package com.fyp.hotel.util;
//
//import java.io.File;
//import java.io.IOException;
//import java.net.URL;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.Optional;
//
//public class VideoUtils {
//
//    /**
//     * Get the filePath.
//     *
//     * @return String.
//     */
//    private String getFilePath() {
//        URL url = this.getClass().getResource(VIDEO);
//        assert url != null;
//        return new File(url.getFile()).getAbsolutePath();
//    }
//
//    /**
//     * Content length.
//     *
//     * @param fileName String.
//     * @return Long.
//     */
//    public Long getFileSize(String fileName) {
//        return Optional.ofNullable(fileName)
//                .map(file -> Paths.get(getFilePath(), file))
//                .map(this::sizeFromFile)
//                .orElse(0L);
//    }
//
//    /**
//     * Getting the size from the path.
//     *
//     * @param path Path.
//     * @return Long.
//     */
//    private Long sizeFromFile(Path path) {
//        try {
//            return Files.size(path);
//        } catch (IOException ioException) {
//            logger.error("Error while getting the file size", ioException);
//        }
//        return 0L;
//    }
//}
