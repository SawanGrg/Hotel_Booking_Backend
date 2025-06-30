package com.fyp.hotel.service.user.blog;

import com.fyp.hotel.dto.blog.BlogCommentDTO;
import com.fyp.hotel.dto.blog.BlogDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserBlogService {
    String postUserBlog(MultipartFile blogImage, BlogDTO blogDTO);
    List<BlogDTO> viewUserBlog();
    BlogDTO getSpecificBlog(long blogId);
    String postBlogComment(long blogId, BlogCommentDTO blogCommentDTO);
    List<BlogCommentDTO> getAllBlogCommentSpecificBlog(long blogId);
}