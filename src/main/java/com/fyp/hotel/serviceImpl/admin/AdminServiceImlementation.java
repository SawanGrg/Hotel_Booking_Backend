package com.fyp.hotel.serviceImpl.admin;

import com.fyp.hotel.dao.admin.AdminDAO;
import com.fyp.hotel.dto.admin.AdminAnalyticsDto;
import com.fyp.hotel.dto.userDto.BlogCommentDTO;
import com.fyp.hotel.dto.userDto.BlogDTO;
import com.fyp.hotel.dto.userDto.IssueReportDTO;
import com.fyp.hotel.model.Blog;
import com.fyp.hotel.model.User;
import com.fyp.hotel.repository.BlogRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImlementation {


    @Autowired
    private AdminDAO adminDAO;
    @Autowired
    private BlogRepository blogRepository;

    @Transactional
    public List<User> getAllUserFilter(String userName, boolean ascending, int page, int size){
        return adminDAO.getAllUserFilter(userName, ascending, page, size);
    }

    @Transactional
    public List<User> getAllVendorFilter(String userName, boolean ascending, int page, int size){
        return adminDAO.getAllVendorFilter(userName, ascending, page, size);
    }

    @Transactional
    public AdminAnalyticsDto getAnalysisData(){

        AdminAnalyticsDto adminAnalyticsDto = new AdminAnalyticsDto();

        adminAnalyticsDto.setTotalUsers(adminDAO.getTotalUsers());
        adminAnalyticsDto.setTotalVendors(adminDAO.getTotalVendors());
        adminAnalyticsDto.setTotalBookings(adminDAO.getTotalBookings());
        adminAnalyticsDto.setTotalHotels(adminDAO.getTotalHotels());
        adminAnalyticsDto.setTotalRooms(adminDAO.getTotalRooms());

        return adminAnalyticsDto;

    }

    @Transactional
    public  List<IssueReportDTO> getAllReports(int page, int size){
        return adminDAO.getAllReports();
    }

    @Transactional
    public List<BlogDTO> getAllBlogsBeforeVerification( int page, int size){
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


    @Transactional
    public String verifyBlog(long blogId, String status) {
        Blog blog = blogRepository.findById(blogId).orElse(null);
        if (blog != null) {
            blog.setStatus(status);
            blogRepository.save(blog);
            return "Blog Verified";
        } else {
            throw new IllegalArgumentException("Blog not found with ID: " + blogId);
        }
    }


}
