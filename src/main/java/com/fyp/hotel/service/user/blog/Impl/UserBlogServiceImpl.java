package com.fyp.hotel.service.user.blog.Impl;

import com.fyp.hotel.dto.blog.BlogCommentDTO;
import com.fyp.hotel.dto.blog.BlogDTO;
import com.fyp.hotel.model.*;
import com.fyp.hotel.repository.BlogCommentRepository;
import com.fyp.hotel.repository.BlogRepository;
import com.fyp.hotel.repository.UserRepository;
import com.fyp.hotel.service.user.blog.UserBlogService;
import com.fyp.hotel.util.FileUploaderHelper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserBlogServiceImpl implements UserBlogService {

    private final BlogRepository blogRepository;
    private final BlogCommentRepository blogCommentRepository;
    private final UserRepository userRepo;
    private final FileUploaderHelper fileUploaderHelper;

    @Autowired
    public UserBlogServiceImpl(BlogRepository blogRepository,
                               BlogCommentRepository blogCommentRepository,
                               UserRepository userRepo,
                               FileUploaderHelper fileUploaderHelper) {
        this.blogRepository = blogRepository;
        this.blogCommentRepository = blogCommentRepository;
        this.userRepo = userRepo;
        this.fileUploaderHelper = fileUploaderHelper;
    }

    @Override
    public String postUserBlog(MultipartFile blogImage, BlogDTO blogDTO) {
        try {
            boolean isImageUploaded = fileUploaderHelper.fileUploader(blogImage);
            if (!isImageUploaded) {
                return "Failed to upload blog image.";
            }

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userName = authentication.getName();
            User user = userRepo.findByUserName(userName);
            if (user == null) {
                return "User not found.";
            }

            Blog blog = createBlogFromDTO(blogDTO, user, blogImage);
            blogRepository.save(blog);
            return "Blog posted successfully.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to post blog.";
        }
    }

    private Blog createBlogFromDTO(BlogDTO blogDTO, User user, MultipartFile blogImage) {
        Blog blog = new Blog();
        blog.setTitle(blogDTO.getTitle());
        blog.setDescription(blogDTO.getDescription());
        blog.setCreatedDate(blogDTO.getCreatedDate());
        blog.setUser(user);
        blog.setStatus("PENDING");
        blog.setBlogTag(blogDTO.getBlogTag());
        blog.setBlogImage("/images/" + blogImage.getOriginalFilename());
        return blog;
    }

    @Override
    public List<BlogDTO> viewUserBlog() {
        return blogRepository.findAll().stream()
                .filter(blog -> "VERIFIED".equals(blog.getStatus()))
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
                .collect(Collectors.toList());
    }

    @Override
    public BlogDTO getSpecificBlog(long blogId) {
        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> new RuntimeException("Blog not found with id: " + blogId));

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
    public String postBlogComment(long blogId, BlogCommentDTO blogCommentDTO) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userName = authentication.getName();
            User user = userRepo.findByUserName(userName);

            Blog blog = blogRepository.findById(blogId)
                    .orElseThrow(() -> new RuntimeException("Blog not found with id: " + blogId));

            BlogComment blogComment = new BlogComment();
            blogComment.setComment(blogCommentDTO.getComment());
            blogComment.setStatus("VERIFIED");
            blogComment.setBlog(blog);
            blogComment.setUser(user);
            blogComment.setCreatedDate(LocalDate.now());

            blogCommentRepository.save(blogComment);
            return "Comment posted successfully.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to post comment.";
        }
    }

    @Override
    public List<BlogCommentDTO> getAllBlogCommentSpecificBlog(long blogId) {
        List<BlogComment> blogComments = this.blogCommentRepository.findAllByBlogId(blogId);
        return blogComments.stream()
                .map(blogComment -> new BlogCommentDTO(
                        blogComment.getCommentId(),
                        blogComment.getComment(),
                        blogComment.getUser(),
                        blogComment.getCreatedDate().toString()
                ))
                .collect(Collectors.toList());
    }
}