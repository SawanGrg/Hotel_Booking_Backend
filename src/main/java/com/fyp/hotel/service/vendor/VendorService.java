package com.fyp.hotel.service.vendor;

import org.springframework.web.multipart.MultipartFile;

import com.fyp.hotel.dto.userDto.UserDto;
import com.fyp.hotel.dto.vendorDto.HotelDto;
import com.fyp.hotel.dto.vendorDto.RoomDto;

public interface VendorService {

    String registerVendor(UserDto user, MultipartFile userImage, HotelDto hotel, MultipartFile hotelImage);
    String addRooms(RoomDto hotel, MultipartFile mainRoomImage, MultipartFile roomImage1, MultipartFile roomImage2, MultipartFile roomImage3);   
}
