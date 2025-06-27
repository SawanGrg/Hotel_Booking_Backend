package com.fyp.hotel.service.admin.blog;

import com.fyp.hotel.dto.userDto.BlogDTO;

import java.util.List;

public interface AdminBlogService {
    List<BlogDTO> getAllBlogsBeforeVerification(int page, int size);
    BlogDTO getSpecificBlog(long blogId);
    String verifyBlog(long blogId, String status);
}
