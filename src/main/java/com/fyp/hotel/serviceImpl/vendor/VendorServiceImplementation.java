package com.fyp.hotel.serviceImpl.vendor;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.fyp.hotel.dao.HotelDAO;
import com.fyp.hotel.dto.CheckRoomAvailabilityDto;
import com.fyp.hotel.dto.vendorDto.VendorDto;
import com.fyp.hotel.model.*;
import com.fyp.hotel.repository.*;
import com.fyp.hotel.util.ValueMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fyp.hotel.dto.userDto.UserDto;
import com.fyp.hotel.dto.vendorDto.HotelDto;
import com.fyp.hotel.dto.vendorDto.RoomDto;
import com.fyp.hotel.service.vendor.VendorService;
import com.fyp.hotel.util.FileUploaderHelper;

import jakarta.transaction.Transactional;

@Service
public class VendorServiceImplementation implements VendorService {

    @Autowired
    @Lazy
    private UserRepository userRepo;
    @Autowired
    @Lazy
    private RoleRepository roleRepo;
    @Autowired
    @Lazy
    private HotelRepository hotelRepo;
    @Autowired
    @Lazy
    private HotelRoomRepository hotelRoomRepo;
    @Autowired
    @Lazy
    RoomImageRepository roomImageRepo;
    @Autowired
    @Lazy
    private FileUploaderHelper fileUploaderHelper;
    @Autowired
    @Lazy
    private PasswordEncoder passwordEncoder;
    @Autowired
    @Lazy
    private ValueMapper valueMapper;
    @Autowired
    @Lazy
    private RoomImageRepository roomImageRepository;
    @Autowired
    @Lazy
    private ReportRepository reportRepository;
    @Autowired
    @Lazy
    private HotelDAO hotelDAO;

    @Override
    @Transactional
    public String registerVendor(VendorDto vendorDto, MultipartFile userImage, HotelDto hotelDto,
                                 MultipartFile hotelImage) {

        System.out.println("user data in dto :step 3 --> " + vendorDto);
        User userObj = valueMapper.conversionToUser(vendorDto);
        System.out.println("user data in object :step 4 --> " + userObj);
        Hotel hotelObj = valueMapper.conversionToHotel(hotelDto);
        System.out.println("hotel data in object :step 5 --> " + hotelObj);

        boolean isUserImageUploaded = fileUploaderHelper.fileUploader(userImage);
        boolean isHotelImageUploaded = fileUploaderHelper.fileUploader(hotelImage);

        if (isUserImageUploaded && isHotelImageUploaded) {

            userObj.setUserProfilePicture("/images/" + userImage.getOriginalFilename());
            hotelObj.setHotelImage(hotelImage.getOriginalFilename());

            Role vendorRole = roleRepo.findByRoleName("ROLE_VENDOR");
            userObj.getRoles().add(vendorRole);

            User currentUser = userRepo.save(userObj);

            if (currentUser == null) {
                return "Vendor Registration Failed";
            }

            hotelObj.setUser(currentUser);
            hotelRepo.save(hotelObj);

            return "Vendor Registered Successfully";
        } else {
            return "Vendor Registration Failed";
        }
    }

    @Override
    @Transactional
    public String addRooms(RoomDto roomData, MultipartFile mainRoomImage, MultipartFile roomImage1,
                           MultipartFile roomImage2, MultipartFile roomImage3) {

        System.out.println("room data : from vendor service " + roomData);
        System.out.println("main room image : from vendor service " + mainRoomImage.getOriginalFilename());
//        System.out.println("room image 1 : " + roomImage1.getOriginalFilename());
//        System.out.println("room image 2 : " + roomImage2.getOriginalFilename());
//        System.out.println("room image 3 : " + roomImage3.getOriginalFilename());

        System.out.println("room data : step 1");
        HotelRoom hotelRoom = valueMapper.conversionToHotelRoom(roomData);
        System.out.println("room data : " + hotelRoom);
        System.out.println("room data : step 2");

        System.out.println("hotel room --> : " + hotelRoom);

        boolean isMainRoomImageUploaded = fileUploaderHelper.fileUploader(mainRoomImage);
        System.out.println("room data : step 3");

        if (isMainRoomImageUploaded) {

            hotelRoom.setMainRoomImage(mainRoomImage.getOriginalFilename());

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            String username = authentication.getName();
            System.out.println("username from vendor service : " + username);

            if (authentication.isAuthenticated()) {
                System.out.println("authentication is not null and is : " + authentication.getPrincipal());
                Hotel hotel = hotelRepo.findByUser(userRepo.findByUserName(username));
                System.out.println("hotel from vendor service before setting the hotel id: " + hotel);
                hotelRoom.setHotel(hotel);
            }
            HotelRoom currentRoom = hotelRoomRepo.save(hotelRoom);
            System.out.println("hotel from vendor service after setting the hotel id: ");
            if (currentRoom == null) {
                return "Room Registration Failed";
            }

            if (roomImage1 != null) {
                boolean isRoomImage1Uploaded = fileUploaderHelper.fileUploader(roomImage1);
                if (isRoomImage1Uploaded) {
                    RoomImage roomImageObj = new RoomImage();
                    roomImageObj.setImageUrl(roomImage1.getOriginalFilename());
                    roomImageObj.setHotelRoom(currentRoom);
                    roomImageObj.setCreatedAt(ZonedDateTime.now());
                    roomImageObj.setImageStatus("ACTIVE");
                    roomImageRepo.save(roomImageObj);
                }
            }

            if (roomImage2 != null) {
                boolean isRoomImage2Uploaded = fileUploaderHelper.fileUploader(roomImage2);
                if (isRoomImage2Uploaded) {
                    RoomImage roomImageObj = new RoomImage();
                    roomImageObj.setImageUrl(roomImage2.getOriginalFilename());
                    roomImageObj.setHotelRoom(currentRoom);
                    roomImageObj.setCreatedAt(ZonedDateTime.now());
                    roomImageObj.setImageStatus("ACTIVE");
                    roomImageRepo.save(roomImageObj);
                }
            }

            if (roomImage3 != null) {
                boolean isRoomImage3Uploaded = fileUploaderHelper.fileUploader(roomImage3);
                if (isRoomImage3Uploaded) {
                    RoomImage roomImageObj = new RoomImage();
                    roomImageObj.setImageUrl(roomImage3.getOriginalFilename());
                    roomImageObj.setHotelRoom(currentRoom);
                    roomImageObj.setCreatedAt(ZonedDateTime.now());
                    roomImageObj.setImageStatus("ACTIVE");
                    roomImageRepo.save(roomImageObj);
                }
            }
        }
        return "Room Registered Successfully";
    }

    //extract all the hotel rooms and their images with pagination
    @Transactional
    public Page<HotelRoom> getHotelRooms(int page, int size) {
        Authentication authenticatedUser = SecurityContextHolder.getContext().getAuthentication();
        String username = authenticatedUser.getName();

        // Fetch the hotel ID for the given username
        Long hotelId = hotelRepo.findByUser(userRepo.findByUserName(username)).getHotelId();

        // Fetch the hotel rooms with pagination
        Pageable pageable = PageRequest.of(page, size);
        Page<HotelRoom> hotelRooms = hotelRoomRepo.findAllByHotel_HotelId(hotelId, pageable);

        // Fetch all room images for each room
        for (HotelRoom room : hotelRooms) {
            List<RoomImage> roomImages = roomImageRepo.findByHotelRoom(room);
            for(RoomImage roomImage : roomImages){
                System.out.println("Room image first : " + roomImages);
                System.out.println("Room image second iteration: " + roomImage);
                room.setRoomImages(roomImages);
            }
        }
        System.out.println("Hotel rooms final: " + hotelRooms);
        return hotelRooms;
    }
    @Transactional
    public List<RoomImage> getRoomImagesOfRoom(Long roomId) {
        // Fetch the specific room based on roomId
        Optional<HotelRoom> optionalHotelRoom = hotelRoomRepo.findById(roomId);
        if (optionalHotelRoom.isPresent()) {
            HotelRoom room = optionalHotelRoom.get();
            return room.getRoomImages();
        }
        return new ArrayList<>(); // Return an empty list if room or images not found
    }

    @Transactional
    public String updateRoom(
            Long roomId,
            RoomDto roomDto,
            MultipartFile mainRoomImage,
            MultipartFile roomImage1,
            MultipartFile roomImage2,
            MultipartFile roomImage3
    ) {
        try {
            System.out.println("room data: from vendor service " + roomDto);
            System.out.println("main room image: from vendor service " + mainRoomImage.getOriginalFilename());
            System.out.println("room image 1: " + roomImage1.getOriginalFilename());
            System.out.println("room image 2: " + roomImage2.getOriginalFilename());
            System.out.println("room image 3: " + roomImage3.getOriginalFilename());

            // Convert DTO to HotelRoom entity
            HotelRoom updatedRoom = valueMapper.conversionToHotelRoom(roomDto);

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication.isAuthenticated()) {
                String vendorName = authentication.getName();

                // Fetch the room based on room ID and vendor name
                Optional<HotelRoom> optionalRoom = hotelRoomRepo.findById(roomId);
                if (optionalRoom.isPresent()) {
                    HotelRoom room = optionalRoom.get();

                    // Update room details
                    room.setRoomNumber(updatedRoom.getRoomNumber().isEmpty() ? room.getRoomNumber() : updatedRoom.getRoomNumber());
                    room.setRoomType(updatedRoom.getRoomType() == null ? room.getRoomType() : updatedRoom.getRoomType());
                    room.setRoomCategory(updatedRoom.getRoomCategory() == null ? room.getRoomCategory() : updatedRoom.getRoomCategory());
                    room.setRoomBed(updatedRoom.getRoomBed() == null ? room.getRoomBed() : updatedRoom.getRoomBed());
                    room.setRoomPrice(updatedRoom.getRoomPrice() == null ? room.getRoomPrice() : updatedRoom.getRoomPrice());
                    room.setRoomDescription(updatedRoom.getRoomDescription().isEmpty() ? room.getRoomDescription() : updatedRoom.getRoomDescription());
                    room.setHasAC(updatedRoom.getHasAC() == null ? room.getHasAC() : updatedRoom.getHasAC());
                    room.setHasBalcony(updatedRoom.getHasBalcony() == null ? room.getHasBalcony() : updatedRoom.getHasBalcony());
                    room.setHasRefridge(updatedRoom.getHasRefridge() == null ? room.getHasRefridge() : updatedRoom.getHasRefridge());
                    room.setHasTV(updatedRoom.getHasTV() == null ? room.getHasTV() : updatedRoom.getHasTV());
                    room.setHasWifi(updatedRoom.getHasWifi() == null ? room.getHasWifi() : updatedRoom.getHasWifi());
                    room.setRoomStatus(updatedRoom.getRoomStatus().isEmpty() ? room.getRoomStatus() : updatedRoom.getRoomStatus());
                    room.setUpdatedAt(ZonedDateTime.now());

                    // Save the updated room
                    hotelRoomRepo.save(room);

                    //update the room image
                    if (!mainRoomImage.isEmpty()) {
                        boolean isMainRoomImageUploaded = fileUploaderHelper.fileUploader(mainRoomImage);
                        if (isMainRoomImageUploaded) {
                            room.setMainRoomImage(mainRoomImage.getOriginalFilename());
                            hotelRoomRepo.save(room);
                        }
                    }

                    //update the side room image
                    if(!roomImage1.isEmpty()){
                        boolean isRoomImage1Uploaded = fileUploaderHelper.fileUploader(roomImage1);
                        if(isRoomImage1Uploaded){
                            RoomImage roomImageObj = new RoomImage();
                            roomImageObj.setImageUrl(roomImage1.getOriginalFilename());
                            roomImageObj.setHotelRoom(room);
                            roomImageObj.setCreatedAt(ZonedDateTime.now());
                            roomImageObj.setImageStatus("ACTIVE");
                            roomImageRepo.save(roomImageObj);
                        }
                    }

                    if(!roomImage2.isEmpty()){
                        boolean isRoomImage2Uploaded = fileUploaderHelper.fileUploader(roomImage2);
                        if(isRoomImage2Uploaded){
                            RoomImage roomImageObj = new RoomImage();
                            roomImageObj.setImageUrl(roomImage2.getOriginalFilename());
                            roomImageObj.setHotelRoom(room);
                            roomImageObj.setCreatedAt(ZonedDateTime.now());
                            roomImageObj.setImageStatus("ACTIVE");
                            roomImageRepo.save(roomImageObj);
                        }
                    }
                    if(!roomImage3.isEmpty()){
                        boolean isRoomImage3Uploaded = fileUploaderHelper.fileUploader(roomImage3);
                        if(isRoomImage3Uploaded){
                            RoomImage roomImageObj = new RoomImage();
                            roomImageObj.setImageUrl(roomImage3.getOriginalFilename());
                            roomImageObj.setHotelRoom(room);
                            roomImageObj.setCreatedAt(ZonedDateTime.now());
                            roomImageObj.setImageStatus("ACTIVE");
                            roomImageRepo.save(roomImageObj);
                        }
                    }
                    return "Room updated successfully";
                } else {
                    return "Room not found for the given ID and vendor";
                }
            } else {
                return "Authentication failed";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to update room";
        }
    }


    @Transactional
    public String deleteRoom(Long roomId) {
        try {
            Authentication authenticatedUser = SecurityContextHolder.getContext().getAuthentication();
            String username = authenticatedUser.getName();
            if (authenticatedUser.isAuthenticated()) {
                // Fetch the hotel ID for the given username
                Long hotelIdFromDb = hotelRepo.findByUser(userRepo.findByUserName(username)).getHotelId();
                System.out.println("Hotel ID from DB: " + hotelIdFromDb);

                //delete room images before deleting the room
                List<RoomImage> roomImages = roomImageRepository.findImageUrlByHotelRoom_RoomId(roomId);
                System.out.println("Room images: " + roomImages);

                for (RoomImage roomImage : roomImages) {
                    System.out.println("Room image: " + roomImage);
                    roomImageRepository.deleteImageUrlByHotelRoom_RoomId(roomId);
                }
                // Delete the room and check if the deletion was successful
                hotelRoomRepo.deleteRoomById(roomId);
                System.out.println("Deleted room ID: " + roomId);

                Optional<HotelRoom> deletedRoom = hotelRoomRepo.findById(roomId);
                System.out.println("Deleted room: " + deletedRoom);

                return "Room deleted successfully";
            }
            return "user is no authenticated";
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to delete room";
        }
    }

    //post report and issue by the vendor
    @Transactional
    public String postReport(Report report){
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication.isAuthenticated()) {
                String username = authentication.getName();

                User vendorObject = userRepo.findByUserName(username);

                report.setUser(vendorObject);

                reportRepository.save(report);

                return "Report posted successfully";
            }
            return "Authentication failed";
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to post report";
        }
    }

    //get hotel details only
    @Transactional
    public Hotel getHotelDetails(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.isAuthenticated()) {
            String username = authentication.getName();
            User vendor = userRepo.findByUserName(username);
            return hotelRepo.findByUser(vendor);
        }
        return null;
    }

    //get all booked or refunded or cancelled bookings
    @Transactional
    public List<CheckRoomAvailabilityDto> getAllBookedDetails(String status) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User vendor = userRepo.findByUserName(username);
        Hotel hotel = hotelRepo.findByUser(vendor);
        List<Booking> bookings = hotelDAO.getBookingDetails(status, hotel.getHotelId());
        List<CheckRoomAvailabilityDto> availabilityList = new ArrayList<>();

        for (Booking booking : bookings) {
            CheckRoomAvailabilityDto availabilityDto = new CheckRoomAvailabilityDto();
            availabilityDto.setRoomId(booking.getHotelRoom().getRoomId());
            if (booking.getCheckInDate() != null)
                availabilityDto.setCheckInDate(booking.getCheckInDate().toString());
            if (booking.getCheckOutDate() != null)
                availabilityDto.setCheckOutDate(booking.getCheckOutDate().toString());
            availabilityDto.setUserName(booking.getUser().getUsername());
            availabilityDto.setRoomNumber(booking.getHotelRoom().getRoomNumber());

            availabilityList.add(availabilityDto);
        }

        return availabilityList;
    }




}
