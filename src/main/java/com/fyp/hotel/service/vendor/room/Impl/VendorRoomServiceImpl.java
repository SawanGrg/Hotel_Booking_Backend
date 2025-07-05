package com.fyp.hotel.service.vendor.room.Impl;

import com.fyp.hotel.dao.hotel.HotelDAO;
import com.fyp.hotel.dto.room.RoomDto;
import com.fyp.hotel.model.Hotel;
import com.fyp.hotel.model.HotelRoom;
import com.fyp.hotel.model.RoomImage;
import com.fyp.hotel.model.User;
import com.fyp.hotel.repository.*;
import com.fyp.hotel.service.vendor.room.VendorRoomService;
import com.fyp.hotel.util.FileUploaderHelper;
import com.fyp.hotel.util.ValueMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class VendorRoomServiceImpl implements VendorRoomService {

    private final RoleRepository roleRepo;
    private final HotelRoomRepository hotelRoomRepository;
    private final RoomImageRepository roomImageRepository;
    private final HotelRepository hotelRepository;
    private final UserRepository userRepository;
    private final FileUploaderHelper fileUploaderHelper;
    private final ValueMapper valueMapper;
    private final HotelDAO hotelDAO;

    @Override
    @Transactional
    public Page<HotelRoom> getHotelRooms(int page, int size) {
        Authentication authenticatedUser = SecurityContextHolder.getContext().getAuthentication();
        String username = authenticatedUser.getName();

        // Fetch the hotel ID for the given username
        Long hotelId = hotelRepository.findByUser(userRepository.findByUserName(username)).getHotelId();

        // Fetch the hotel rooms with pagination
        Pageable pageable = PageRequest.of(page, size);
        Page<HotelRoom> hotelRooms = hotelRoomRepository.findAllByHotel_HotelId(hotelId, pageable);

        // Fetch all room images for each room
        for (HotelRoom room : hotelRooms) {
            List<RoomImage> roomImages = roomImageRepository.findByHotelRoom(room);
            for (RoomImage roomImage : roomImages) {
                room.setRoomImages(roomImages);
            }
        }
        return hotelRooms;
    }

    @Override
    public String addRooms(RoomDto roomData, MultipartFile mainRoomImage,
                           MultipartFile roomImage1, MultipartFile roomImage2,
                           MultipartFile roomImage3) {

        if (!fileUploaderHelper.fileUploader(mainRoomImage)) {
            return "Room Registration Failed - Main image upload error";
        }

        HotelRoom hotelRoom = valueMapper.conversionToHotelRoom(roomData);
        hotelRoom.setMainRoomImage(mainRoomImage.getOriginalFilename());
        hotelRoom.setHotel(getCurrentVendorHotel());
        hotelRoom.setIsDeleted(false);

        HotelRoom savedRoom = hotelRoomRepository.save(hotelRoom);

        saveRoomImageIfPresent(roomImage1, savedRoom);
        saveRoomImageIfPresent(roomImage2, savedRoom);
        saveRoomImageIfPresent(roomImage3, savedRoom);

        return "Room Registered Successfully";
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoomImage> getRoomImagesOfRoom(Long roomId) {
        return hotelRoomRepository.findById(roomId)
                .map(roomImageRepository::findByHotelRoom)
                .orElse(new ArrayList<>());
    }

    @Override
    public String updateRoom(Long roomId, RoomDto roomDto,
                             MultipartFile mainRoomImage, MultipartFile roomImage1,
                             MultipartFile roomImage2, MultipartFile roomImage3) {

        HotelRoom room = hotelRoomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        updateRoomDetails(room, valueMapper.conversionToHotelRoom(roomDto));

        if (mainRoomImage != null && fileUploaderHelper.fileUploader(mainRoomImage)) {
            room.setMainRoomImage(mainRoomImage.getOriginalFilename());
        }

        hotelRoomRepository.save(room);

        saveRoomImageIfPresent(roomImage1, room);
        saveRoomImageIfPresent(roomImage2, room);
        saveRoomImageIfPresent(roomImage3, room);

        return "Room updated successfully";
    }

    @Override
    public String deleteSpecificRoom(long roomId) {
        HotelRoom room = hotelRoomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));
        room.setIsDeleted(true);
        hotelRoomRepository.save(room);
        return "Room deleted successfully";
    }

    @Override
    @Transactional(readOnly = true)
    public Boolean checkRoomExistsOrNot(String roomNumber) {
        String vendorName = SecurityContextHolder.getContext().getAuthentication().getName();
        Hotel hotel = hotelRepository.findByUser(userRepository.findByUserName(vendorName));
        return hotelDAO.RoomExistOrNot(roomNumber, hotel.getHotelId());
    }

    @Override
    @Transactional(readOnly = true)
    public List<HotelRoom> getRoomDetails(Long roomId) {
        List<HotelRoom> rooms = new ArrayList<>();
        hotelRoomRepository.findById(roomId).ifPresent(rooms::add);
        return rooms;
    }

    private Hotel getCurrentVendorHotel() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User vendor = userRepository.findByUserName(username);
        return hotelRepository.findByUser(vendor);
    }

    private void saveRoomImageIfPresent(MultipartFile image, HotelRoom room) {
        if (image != null && fileUploaderHelper.fileUploader(image)) {
            RoomImage roomImage = new RoomImage();
            roomImage.setImageUrl(image.getOriginalFilename());
            roomImage.setHotelRoom(room);
            roomImage.setCreatedAt(ZonedDateTime.now());
            roomImage.setImageStatus("ACTIVE");
            roomImageRepository.save(roomImage);
        }
    }

    private void updateRoomDetails(HotelRoom room, HotelRoom updatedRoom) {
        if (updatedRoom.getRoomNumber() != null) {
            room.setRoomNumber(updatedRoom.getRoomNumber());
        }
        if (updatedRoom.getRoomType() != null) {
            room.setRoomType(updatedRoom.getRoomType());
        }
        if (updatedRoom.getRoomCategory() != null) {
            room.setRoomCategory(updatedRoom.getRoomCategory());
        }
        if (updatedRoom.getRoomBed() != null) {
            room.setRoomBed(updatedRoom.getRoomBed());
        }
        if (updatedRoom.getRoomPrice() != null) {
            room.setRoomPrice(updatedRoom.getRoomPrice());
        }
        if (updatedRoom.getRoomDescription() != null) {
            room.setRoomDescription(updatedRoom.getRoomDescription());
        }
        if (updatedRoom.getHasAC() != null) {
            room.setHasAC(updatedRoom.getHasAC());
        }
        if (updatedRoom.getHasBalcony() != null) {
            room.setHasBalcony(updatedRoom.getHasBalcony());
        }
        if (updatedRoom.getHasRefridge() != null) {
            room.setHasRefridge(updatedRoom.getHasRefridge());
        }
        if (updatedRoom.getHasTV() != null) {
            room.setHasTV(updatedRoom.getHasTV());
        }
        if (updatedRoom.getHasWifi() != null) {
            room.setHasWifi(updatedRoom.getHasWifi());
        }
        if (updatedRoom.getRoomStatus() != null) {
            room.setRoomStatus(updatedRoom.getRoomStatus());
        }
        room.setUpdatedAt(ZonedDateTime.now());
    }
}