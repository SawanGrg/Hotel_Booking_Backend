// AdminBlogController.java
package com.fyp.hotel.controller.admin.blog;

import com.fyp.hotel.dto.common.ApiResponse;
import com.fyp.hotel.dto.blog.BlogDTO;
import com.fyp.hotel.dto.common.PaginationRequestDTO;
import com.fyp.hotel.service.admin.AdminServiceFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/admin")
public class AdminBlogController {

    @Autowired
    private AdminServiceFacade adminServiceFacade;

    @GetMapping("/BlogBeforeVerification")
    public ResponseEntity<?> viewAllBlogBeforeVerification(
            @ModelAttribute PaginationRequestDTO paginationRequestDTO
    ) {
        List<BlogDTO> blogs = adminServiceFacade.blogService.getAllBlogsBeforeVerification(paginationRequestDTO.getPageNumber(), paginationRequestDTO.getPageSize());
        return ResponseEntity.ok(new ApiResponse<>(200, "Success", blogs));
    }

    @GetMapping("/specificBlog/{blogId}")
    public ResponseEntity<?> getSpecificBlog(
            @PathVariable long blogId
    ) {
        BlogDTO blog = adminServiceFacade.blogService.getSpecificBlog(blogId);
        return ResponseEntity.ok(new ApiResponse<>(200, "Success", blog));
    }

    @PostMapping("/verifyBlog/{blogId}")
    public ResponseEntity<?> verifyBlog(
            @PathVariable long blogId,
            @RequestParam(defaultValue = "VERIFIED") String status
    ) {
        String response = adminServiceFacade.blogService.verifyBlog(blogId, status);
        return ResponseEntity.ok(new ApiResponse<>(200, "Success", response));
    }
}