package com.fyp.hotel.service.user;

import com.fyp.hotel.dto.CheckRoomAvailabilityDto;
import com.fyp.hotel.dto.DisplayHotelWithAmenitiesDto;
import com.fyp.hotel.dto.khalti.KhaltiResponseDTO;
import com.fyp.hotel.dto.userDto.BlogDTO;
import com.fyp.hotel.dto.userDto.BookDto;
import com.fyp.hotel.dto.userDto.HotelReviewDTO;
import com.fyp.hotel.dto.userDto.UserChangePasswordDto;
import com.fyp.hotel.model.Hotel;
import com.fyp.hotel.model.HotelRoom;
import com.fyp.hotel.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;

public interface UserService {

    @Transactional
    boolean checkUserExist(String userName);

    @Transactional
    String registerUser(User user, MultipartFile userImage);

    @Transactional
    String verifyOtp(String otp);

    @Transactional
    List<Hotel> getAllHotels(int page, int size);

    @Transactional
    List<DisplayHotelWithAmenitiesDto> getAllHotelsWithAmenities(String hotelName, String hotelLocation, int page, int size);

    @Transactional
    String updateDetails(
            String userName,
            String userFirstName,
            String userLastName,
            String userEmail,
            String userPhone,
            String userAddress,
            String dateOfBirth,
            MultipartFile userProfilePicture
    );

    @Transactional
    String changePassword(UserChangePasswordDto userChangePasswordDto);

    @Transactional
    String clearOutJwtToken();

    @Transactional
    List<HotelRoom> getAllRoomsOfHotel(Long hotelId, int page, int size);

    User getUserById();

    @Transactional
    String hotelPaymentGateWay(@Validated BookDto bookDto);

    @Transactional
    KhaltiResponseDTO ePaymentGateway(@Validated BookDto bookDto);

    @Transactional
    String updatePaymentTable(String pidx, String status, String bookingId, long totalAmount);

    @Transactional
    List<HotelRoom> filterRooms(
            Long hotelId,
            String roomType,
            String roomCategory,
            String roomBed,
            String minRoomPrice,
            String maxRoomPrice,
            Boolean hasAC,
            Boolean hasBalcony,
            Boolean hasRefridge,
            int page,
            int size
    );

    @Transactional
    List<Hotel> getHotelBasedOnNameOrLocation(String hotelName, String hotelLocation, int page, int size);

    @Transactional
    CheckRoomAvailabilityDto checkRoomAvailability(Long roomId);

    @Transactional
    String postHotelReviewByUser(long hotelId, HotelReviewDTO hotelReviewDTO);

    @Transactional
    List<HotelReviewDTO> getAllHotelReviews(Long hotelId);

    @Transactional
    String postUserBlog(MultipartFile blogImage, BlogDTO blogDTO);
}
