package com.fyp.hotel.service.user.hotel.Impl;

import com.fyp.hotel.dao.hotel.HotelDAO;
import com.fyp.hotel.dao.hotel.HotelRoomDAO;
import com.fyp.hotel.dto.hotel.DisplayHotelWithAmenitiesDto;
import com.fyp.hotel.model.Hotel;
import com.fyp.hotel.model.HotelRoom;
import com.fyp.hotel.model.RoomImage;
import com.fyp.hotel.repository.HotelRepository;
import com.fyp.hotel.repository.HotelRoomRepository;
import com.fyp.hotel.repository.RoomImageRepository;
import com.fyp.hotel.service.user.hotel.UserHotelService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class UserHotelServiceImpl implements UserHotelService {

    private final HotelRepository hotelRepo;
    private final HotelRoomRepository hotelRoomRepository;
    private final RoomImageRepository roomImageRepository;
    private final HotelDAO hotelDAO;
    private final HotelRoomDAO hotelRoomDAO;

    @Autowired
    public UserHotelServiceImpl(
            HotelRepository hotelRepo,
            HotelRoomRepository hotelRoomRepository,
            RoomImageRepository roomImageRepository,
            HotelDAO hotelDAO,
            HotelRoomDAO hotelRoomDAO) {
        this.hotelRepo = hotelRepo;
        this.hotelRoomRepository = hotelRoomRepository;
        this.roomImageRepository = roomImageRepository;
        this.hotelDAO = hotelDAO;
        this.hotelRoomDAO = hotelRoomDAO;
    }

    @Override
    public List<Hotel> getAllHotels(int page, int size) {
        return this.hotelRepo.findAll();
    }

    @Override
    public List<DisplayHotelWithAmenitiesDto> getAllHotelsWithAmenities(String hotelName, String hotelLocation, int page, int size) {
        return this.hotelDAO.getHotelWithAmenitiesAndRating(hotelName, hotelLocation);
    }

    @Override
    public List<HotelRoom> getAllRoomsOfHotel(Long hotelId, int page, int size) {
        List<HotelRoom> roomDatas = this.hotelRoomRepository.findActiveHotelRoomsByHotelId(hotelId);
        List<RoomImage> roomImages = this.roomImageRepository.findByHotelRoom_RoomId(hotelId);

        for (HotelRoom roomData : roomDatas) {
            for (RoomImage roomImage : roomImages) {
                if (roomData.getRoomId() == roomImage.getHotelRoom().getRoomId()) {
                    roomData.getRoomImages().add(roomImage);
                }
            }
        }
        return roomDatas;
    }

    @Override
    public List<Hotel> getHotelBasedOnNameOrLocation(String hotelName, String hotelLocation, int page, int size) {
        return hotelDAO.getHotelsBasedOnCondition(hotelName, hotelLocation, page, size);
    }

    @Override
    public List<HotelRoom> filterRooms(Long hotelId, String roomType, String roomCategory, String roomBed,
                                       String minRoomPrice, String maxRoomPrice, Boolean hasAC,
                                       Boolean hasBalcony, Boolean hasRefridge, int page, int size) {
        return hotelRoomDAO.getHotelRooms(
                hotelId,
                roomType,
                roomCategory,
                roomBed,
                minRoomPrice,
                maxRoomPrice,
                hasAC,
                hasBalcony,
                hasRefridge,
                page,
                size
        );
    }

    @Override
    public String getUserNameOfHotel(long hotelId) {
        return hotelDAO.getHotelOwnerName(hotelId);
    }
}