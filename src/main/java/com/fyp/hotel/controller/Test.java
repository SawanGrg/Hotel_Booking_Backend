package com.fyp.hotel.controller;

//import com.fyp.hotel.util.VideoUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.io.ByteArrayResource;
//import org.springframework.core.io.Resource;
//import org.springframework.http.*;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//import jakarta.servlet.http.HttpServletRequest;
//
//import java.io.IOException;
//
//@RestController
//public class VideoController {
//
//    private final VideoUtils videoUtils;
//
//    @Autowired
//    public VideoController(VideoUtils videoUtils) {
//        this.videoUtils = videoUtils;
//    }
//
//    @PostMapping("/uploadVideo")
//    public ResponseEntity<String> uploadVideo(@RequestParam("file") MultipartFile file) {
//        if (file.isEmpty()) {
//            return new ResponseEntity<>("Please select a file to upload", HttpStatus.BAD_REQUEST);
//        }
//        try {
//            videoUtils.processFile(file);
//            return new ResponseEntity<>("File uploaded successfully", HttpStatus.OK);
//        } catch (Exception e) {
//            return new ResponseEntity<>("Failed to upload file: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//
//    @GetMapping("/video")
//    public ResponseEntity<Resource> streamVideo(
//            @RequestParam("filename") String filename,
//            @RequestHeader(value = "Range", required = false) String rangeHeader) throws IOException {
//
//        // Load video content for the specified filename
//        byte[] videoContent = videoUtils.readByteRange(filename, rangeHeader);
//
//        // Create ByteArrayResource from video content
//        ByteArrayResource resource = new ByteArrayResource(videoContent);
//
//        // If Range header is provided, respond with partial content (206 Partial Content)
//        if (rangeHeader != null && rangeHeader.startsWith("bytes=")) {
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
//            headers.setContentLength(videoContent.length);
//            headers.set(HttpHeaders.ACCEPT_RANGES, "bytes");
//            headers.setCacheControl(CacheControl.noCache().getHeaderValue());
//
//            // Parse range header
//            String[] ranges = rangeHeader.substring(6).split("-");
//            long start = Long.parseLong(ranges[0]);
//            long end = ranges.length > 1 ? Long.parseLong(ranges[1]) : videoContent.length - 1;
//
//            // Set Content-Range header to indicate the range of bytes being returned
////            headers.setContentRange("bytes " + start + "-" + end + "/" + videoContent.length);
//
//            // Create Partial Content response with the appropriate range
//            return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
//                    .headers(headers);
//                    .body(new ByteArrayResource(videoContent, (int) start, (int) (end - start + 1)));
//        } else {
//            // If no Range header provided, return entire video content (200 OK)
//            return ResponseEntity.ok()
//                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
//                    .body(resource);
//        }
//    }
//}
