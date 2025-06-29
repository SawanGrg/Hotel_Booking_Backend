package com.fyp.hotel.service.admin.blog.Impl;

import com.fyp.hotel.dto.blog.BlogDTO;
import com.fyp.hotel.repository.BlogRepository;
import com.fyp.hotel.service.admin.blog.AdminBlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fyp.hotel.model.Blog;

import java.util.List;

@Service
public class AdminBlogServiceImpl implements AdminBlogService {

    @Autowired
    private BlogRepository blogRepository;

    @Override
    public List<BlogDTO> getAllBlogsBeforeVerification(int page, int size) {
        return blogRepository.findAll().stream()
                .map(blog -> new BlogDTO(
                        blog.getBlogId(),
                        blog.getTitle(),
                        blog.getDescription(),
                        blog.getBlogTag(),
                        blog.getBlogImage(),
                        blog.getStatus(),
                        blog.getCreatedDate(),
                        blog.getUser()
                ))
                .toList();
    }

    @Override
    public BlogDTO getSpecificBlog(long blogId) {
        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> new IllegalArgumentException("Blog not found"));
        return new BlogDTO(
                blog.getBlogId(),
                blog.getTitle(),
                blog.getDescription(),
                blog.getBlogTag(),
                blog.getBlogImage(),
                blog.getStatus(),
                blog.getCreatedDate(),
                blog.getUser()
        );
    }

    @Override
    public String verifyBlog(long blogId, String status) {
        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> new IllegalArgumentException("Blog not found"));
        blog.setStatus(status);
        blogRepository.save(blog);
        return "Blog Verified";
    }
}
