package com.fyp.hotel.service.user;

import com.fyp.hotel.dto.room.CheckRoomAvailabilityDto;
import com.fyp.hotel.dto.hotel.DisplayHotelWithAmenitiesDto;
import com.fyp.hotel.dto.khalti.KhaltiResponseDTO;
import com.fyp.hotel.dto.blog.BlogCommentDTO;
import com.fyp.hotel.dto.blog.BlogDTO;
import com.fyp.hotel.dto.booking.BookDto;
import com.fyp.hotel.dto.booking.BookingDTO;
import com.fyp.hotel.dto.booking.BookingStatusDTO;
import com.fyp.hotel.dto.hotel.HotelReviewDTO;
import com.fyp.hotel.dto.user.*;
import com.fyp.hotel.model.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {

    // Existing methods
    boolean checkUserExist(String userName);
    String registerUser(User user, MultipartFile userImage);
    String verifyOtp(String otp);
    List<Hotel> getAllHotels(int page, int size);
    List<DisplayHotelWithAmenitiesDto> getAllHotelsWithAmenities(String hotelName, String hotelLocation, int page, int size);
    String updateDetails(String userName, String userFirstName, String userLastName,
                         String userEmail, String userPhone, String userAddress,
                         String dateOfBirth, MultipartFile userProfilePicture);
    String changePassword(UserChangePasswordDto userChangePasswordDto);
    String clearOutJwtToken();
    List<HotelRoom> getAllRoomsOfHotel(Long hotelId, int page, int size);
    User getUserById();
    String cashOnArrivalTransaction(BookDto bookDto);
    KhaltiResponseDTO onlinePaymentTransaction(BookDto bookDto);
    String updatePaymentTransaction(String pidx, String status, String bookingId, long totalAmount);
    List<HotelRoom> filterRooms(Long hotelId, String roomType, String roomCategory,
                                String roomBed, String minRoomPrice, String maxRoomPrice,
                                Boolean hasAC, Boolean hasBalcony, Boolean hasRefridge,
                                int page, int size);
    List<Hotel> getHotelBasedOnNameOrLocation(String hotelName, String hotelLocation, int page, int size);
    CheckRoomAvailabilityDto checkRoomAvailability(Long roomId);
    String postHotelReviewByUser(long hotelId, HotelReviewDTO hotelReviewDTO);
    List<HotelReviewDTO> getAllHotelReviews(Long hotelId);
    String postUserBlog(MultipartFile blogImage, BlogDTO blogDTO);

    // Missing methods from implementation
    List<BlogDTO> viewUserBlog();
    BlogDTO getSpecificBlog(long blogId);
    String postBlogComment(long blogId, BlogCommentDTO blogCommentDTO);
    List<BlogCommentDTO> getAllBlogCommentSpecificBlog(long blogId);
    List<BookingDTO> viewBookingDetails();
    List<BookingStatusDTO> getBookingStatus(Long hotelId);
    String getUserNameOfHotel(long hotelId);
    String postUserMessage(UserMessageDTO userMessageDTO);
}