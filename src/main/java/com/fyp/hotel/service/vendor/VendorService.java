package com.fyp.hotel.service.vendor;

import com.fyp.hotel.dto.hotel.HotelDto;
import com.fyp.hotel.dto.room.RoomDto;
import com.fyp.hotel.dto.vendorDto.*;
import com.fyp.hotel.model.Hotel;
import com.fyp.hotel.model.HotelRoom;
import com.fyp.hotel.model.Report;
import com.fyp.hotel.model.RoomImage;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface VendorService {

    // Method to check if hotel PAN exists in the database
    Boolean checkHotelPanExist(String hotelPan);

    // Method to register a vendor with the associated user and hotel details
    String registerVendor(VendorDto vendorDto, MultipartFile userImage, HotelDto hotelDto, MultipartFile hotelImage);

    // Method to add rooms to a hotel, including multiple images
    String addRooms(RoomDto roomData, MultipartFile mainRoomImage, MultipartFile roomImage1, MultipartFile roomImage2, MultipartFile roomImage3);

    // Method to retrieve hotel rooms with pagination
    Page<HotelRoom> getHotelRooms(int page, int size);

    // Method to retrieve room images for a specific room
    List<RoomImage> getRoomImagesOfRoom(Long roomId);

    // Method to update room details, including room images
    String updateRoom(Long roomId, RoomDto roomDto, MultipartFile mainRoomImage, MultipartFile roomImage1, MultipartFile roomImage2, MultipartFile roomImage3);

    // Method to delete a specific room from the hotel
    String deleteSpecificRoom(long roomId);

    // Method to post a report or issue by the vendor
    String postReport(Report report);

    // Method to get the hotel details for the logged-in vendor
    Hotel getHotelDetailsService();

    // Method to get all bookings for a vendor with a specific status (e.g., pending, booked, refunded, cancelled)
    List<VendorBookingDTO> getBookings(String status, int page, int size);

    // Method to update the booking status
    String updateBooking(long bookingId, long userId, String status);

    // Method to retrieve vendor dashboard analytics
    VendorDashboardAnalyticsDTO getVendorAnalyticsService();
}
