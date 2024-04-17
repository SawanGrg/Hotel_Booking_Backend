package com.fyp.hotel.serviceImpl.admin;

import com.fyp.hotel.dao.HotelDAO;
import com.fyp.hotel.dao.admin.AdminDAO;
import com.fyp.hotel.dto.BookingDTO;
import com.fyp.hotel.dto.admin.AdminAnalyticsDto;
import com.fyp.hotel.dto.admin.AdminRevenueDTO;
import com.fyp.hotel.dto.userDto.BlogCommentDTO;
import com.fyp.hotel.dto.userDto.BlogDTO;
import com.fyp.hotel.dto.userDto.IssueReportDTO;
import com.fyp.hotel.dto.vendorDto.HotelDto;
import com.fyp.hotel.model.Blog;
import com.fyp.hotel.model.Hotel;
import com.fyp.hotel.model.User;
import com.fyp.hotel.repository.BlogRepository;
import com.fyp.hotel.repository.HotelRepository;
import com.fyp.hotel.repository.HotelRoomRepository;
import com.fyp.hotel.repository.UserRepository;
import jakarta.persistence.Id;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdminServiceImlementation {


    @Autowired
    private AdminDAO adminDAO;
    @Autowired
    private BlogRepository blogRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private HotelRepository hotelRepository;
    @Autowired
    private HotelDAO hotelDAO;

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

    //get specific blog from the blog id
    @Transactional
    public BlogDTO getSpecificBlog(long blogId){
        Blog blog = blogRepository.findById(blogId).orElse(null);
        if(blog != null){
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
        }else{
            throw new IllegalArgumentException("Blog not found with ID: " + blogId);
        }
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

    @Transactional
    public List<HotelDto> getAllHotels(int page, int size){

        List<Hotel> hotels = this.adminDAO.getAllActiveHotels(page, size);

        List<HotelDto> hotelDtos = new ArrayList<>();

        hotels.stream().forEach(hotel -> {
            hotelDtos.add(new HotelDto(
                    hotel.getHotelId(),
                    hotel.getHotelName(),
                    hotel.getHotelAddress(),
                    hotel.getHotelContact(),
                    hotel.getHotelEmail(),
                    hotel.getHotelPan(),
                    hotel.getHotelDescription(),
                    hotel.getHotelStar(),
                    hotel.getIsDeleted(),
                    hotel.getCreatedAt().toString(),
                    hotel.getUpdatedAt().toString()
            ));
        });
        return hotelDtos;
    }

    @Transactional
    public List<BookingDTO> getSpecificUserHotelBooking(long userId, int page, int size){
        return adminDAO.getUserBookings(userId, page, size);
    }


    @Transactional
    public AdminRevenueDTO getAdminRevenue(){

        AdminRevenueDTO adminRevenueDTO = new AdminRevenueDTO();

        long totalCash = adminDAO.getTotalCashOnArrivalRevenue();
        long totalKhalti = adminDAO.getTotalKhaltiRevenue();
        adminRevenueDTO.setCashOnArrival(totalCash);
        adminRevenueDTO.setKhaltiPayment(totalKhalti);

        return adminRevenueDTO;
    }

    @Transactional
    public User getUserProfile(long userId){
        return adminDAO.getUserProfile(userId);
    }


    @Transactional
    public String unverifyUser(long userId, String status) {
        try {
            // Fetch user details using DAO method
            User user = adminDAO.getUserProfile(userId);

            // Check if user exists
            if (user == null) {
                return "User not found";
            }

            // If the user is a vendor
            if (user.getRoles().iterator().next().getRoleName().equals("ROLE_VENDOR")) {

                Hotel preHotel = hotelRepository.findByUser(user);
                if (preHotel != null) {

                    // Set hotel status and delete flag
                    preHotel.setIsDeleted(true);
                    preHotel.setHotelStatus(status);

                    // Save updated hotel details using DAO method
                    Hotel savedHotel = hotelDAO.updateHotel(preHotel);
                    if (savedHotel != null) {
                        System.out.println("Hotel: " + savedHotel.getHotelName());
                        System.out.println("Hotel: " + savedHotel.getHotelId());
                        System.out.println("Hotel: " + savedHotel.getHotelAddress());
                        System.out.println("Hotel: " + savedHotel.getIsDeleted());
                    } else {
                        System.out.println("Saved hotel object is null");
                    }
                } else {
                    System.out.println("PreHotel object is null");
                }
            }

            // Set user status
            user.setUserStatus(status);
            userRepository.save(user);

            // Return appropriate message
            if (user.getRoles().iterator().next().getRoleName().equals("ROLE_VENDOR")) {
                return "Vendor Unverified";
            } else {
                return "User Unverified";
            }
        } catch (Exception e) {
            // Log the exception for debugging purposes
            e.printStackTrace();
            // Return an error message
            return "An error occurred while processing the request";
        }
    }






    @Transactional
    public String unVerifyVendor(long userId, String status){
        User user = adminDAO.getUserProfile(userId);
        if(user != null){
            user.setUserStatus(status);
            userRepository.save(user);
            return "Vendor Unverified";
        }else{
            return "Vendor not found";
        }
    }


}
