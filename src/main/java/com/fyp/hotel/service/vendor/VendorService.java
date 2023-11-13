package com.fyp.hotel.service.vendor;

import com.fyp.hotel.dto.vendorDto.VendorDto;
import com.fyp.hotel.model.HotelRoom;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.fyp.hotel.dto.userDto.UserDto;
import com.fyp.hotel.dto.vendorDto.HotelDto;
import com.fyp.hotel.dto.vendorDto.RoomDto;

import java.util.List;

public interface VendorService {

    String registerVendor(VendorDto user, MultipartFile userImage, HotelDto hotel, MultipartFile hotelImage);
    String addRooms(RoomDto hotel, MultipartFile mainRoomImage, MultipartFile roomImage1, MultipartFile roomImage2, MultipartFile roomImage3);
//    List<HotelRoom> getHotelRooms();
    Page<HotelRoom> getHotelRooms(int page, int size);
}
