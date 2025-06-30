package com.fyp.hotel.service.user.hotel;


import com.fyp.hotel.dto.hotel.DisplayHotelWithAmenitiesDto;
import com.fyp.hotel.model.Hotel;
import com.fyp.hotel.model.HotelRoom;

import java.util.List;

public interface UserHotelService {
    List<Hotel> getAllHotels(int page, int size);
    List<DisplayHotelWithAmenitiesDto> getAllHotelsWithAmenities(String hotelName, String hotelLocation, int page, int size);
    List<HotelRoom> getAllRoomsOfHotel(Long hotelId, int page, int size);
    List<Hotel> getHotelBasedOnNameOrLocation(String hotelName, String hotelLocation, int page, int size);
    List<HotelRoom> filterRooms(Long hotelId, String roomType, String roomCategory,
                                String roomBed, String minRoomPrice, String maxRoomPrice,
                                Boolean hasAC, Boolean hasBalcony, Boolean hasRefridge,
                                int page, int size);
    String getUserNameOfHotel(long hotelId);
}