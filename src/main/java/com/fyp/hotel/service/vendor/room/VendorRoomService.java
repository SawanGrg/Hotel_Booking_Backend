package com.fyp.hotel.service.vendor.room;

import com.fyp.hotel.dto.room.RoomDto;
import com.fyp.hotel.model.HotelRoom;
import com.fyp.hotel.model.RoomImage;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface VendorRoomService {
    String addRooms(RoomDto roomData, MultipartFile mainRoomImage,
                    MultipartFile roomImage1, MultipartFile roomImage2, MultipartFile roomImage3);
    Page<HotelRoom> getHotelRooms(int page, int size);
    List<RoomImage> getRoomImagesOfRoom(Long roomId);
    String updateRoom(Long roomId, RoomDto roomDto, MultipartFile mainRoomImage,
                      MultipartFile roomImage1, MultipartFile roomImage2, MultipartFile roomImage3);
    String deleteSpecificRoom(long roomId);
    Boolean checkRoomExistsOrNot(String roomNumber);
    List<HotelRoom> getRoomDetails(Long roomId);
}