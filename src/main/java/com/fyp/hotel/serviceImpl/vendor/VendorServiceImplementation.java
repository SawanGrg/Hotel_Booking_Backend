package com.fyp.hotel.serviceImpl.vendor;

import java.time.Instant;
import java.time.ZonedDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fyp.hotel.dto.userDto.UserDto;
import com.fyp.hotel.dto.vendorDto.HotelDto;
import com.fyp.hotel.dto.vendorDto.RoomDto;
import com.fyp.hotel.model.Hotel;
import com.fyp.hotel.model.HotelBedType;
import com.fyp.hotel.model.HotelRoom;
import com.fyp.hotel.model.HotelRoomCategory;
import com.fyp.hotel.model.HotelRoomType;
import com.fyp.hotel.model.Role;
import com.fyp.hotel.model.RoomImage;
import com.fyp.hotel.model.User;
import com.fyp.hotel.repository.HotelRepository;
import com.fyp.hotel.repository.HotelRoomRepository;
import com.fyp.hotel.repository.RoleRepository;
import com.fyp.hotel.repository.RoomImageRepository;
import com.fyp.hotel.repository.UserRepository;
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

    @Override
    @Transactional
    public String registerVendor(UserDto userDto, MultipartFile userImage, HotelDto hotelDto,
            MultipartFile hotelImage) {

        User userObj = conversionToUser(userDto);
        Hotel hotelObj = conversionToHotel(hotelDto);

        boolean isUserImageUploaded = fileUploaderHelper.fileUploader(userImage);
        boolean isHotelImageUploaded = fileUploaderHelper.fileUploader(hotelImage);

        if (isUserImageUploaded && isHotelImageUploaded) {

            userObj.setUserProfilePicture(userImage.getOriginalFilename());
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

    // conversion of the userDto to user
    private User conversionToUser(UserDto userDto) {
        User user = new User();
        user.setUserName(userDto.getUserName());
        user.setUserFirstName(userDto.getUserFirstName());
        user.setUserLastName(userDto.getUserLastName());
        user.setUserEmail(userDto.getUserEmail());
        user.setPassword(passwordEncoder.encode(userDto.getUserPassword()));
        user.setUserPhone(userDto.getUserPhone());
        user.setUserAddress(userDto.getUserAddress());
        user.setDateOfBirth(userDto.getUserDateOfBirth());
        user.setUserStatus("ACTIVE");
        user.setCreatedAt(Instant.now()); // instant is a class that is used to get the current time
        return user;
    }

    // conversion of the hotelDto to hotel
    private Hotel conversionToHotel(HotelDto hotelDto) {
        Hotel hotel = new Hotel();
        hotel.setHotelName(hotelDto.getHotelName());
        hotel.setHotelAddress(hotelDto.getHotelAddress());
        hotel.setHotelContact(hotelDto.getHotelContact());
        hotel.setHotelEmail(hotelDto.getHotelEmail());
        hotel.setHotelPan(hotelDto.getHotelPan());
        hotel.setHotelStatus("ACTIVE");
        hotel.setCreatedAt(Instant.now());
        hotel.setUpdatedAt(Instant.now());
        return hotel;
    }

    @Override
    @Transactional
    public String addRooms(RoomDto roomData, MultipartFile mainRoomImage, MultipartFile roomImage1,
            MultipartFile roomImage2, MultipartFile roomImage3) {

        System.out.println("room data : from vendor service " + roomData);
        System.out.println("main room image : from vendor service " + mainRoomImage.getOriginalFilename());
        System.out.println("room image 1 : " + roomImage1.getOriginalFilename());
        System.out.println("room image 2 : " + roomImage2.getOriginalFilename());
        System.out.println("room image 3 : " + roomImage3.getOriginalFilename());

        HotelRoom hotelRoom = conversionToHotelRoom(roomData);

        System.out.println("hotel room --> : " + hotelRoom);

        boolean isMainRoomImageUploaded = fileUploaderHelper.fileUploader(mainRoomImage);

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
            

            if (!roomImage1.isEmpty()) {
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

            if (!roomImage2.isEmpty()) {
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

            if (!roomImage3.isEmpty()) {
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

    private HotelRoom conversionToHotelRoom(RoomDto roomDto) {

        HotelRoom hotelRoom = new HotelRoom();
        hotelRoom.setRoomNumber(roomDto.getRoomNumber());

        // Convert String values to enumerated values
        // .valueof is used to convert string to enum it also checks the case of the
        // string
        HotelRoomType roomType = HotelRoomType.valueOf(roomDto.getRoomType());
        HotelRoomCategory roomCategory = HotelRoomCategory.valueOf(roomDto.getRoomCategory());
        HotelBedType roomBed = HotelBedType.valueOf(roomDto.getRoomBed());

        hotelRoom.setRoomType(roomType);
        hotelRoom.setRoomCategory(roomCategory);
        hotelRoom.setRoomBed(roomBed);

        hotelRoom.setRoomPrice(roomDto.getRoomPrice());
        hotelRoom.setRoomDescription(roomDto.getRoomDescription());

        // boolean values
        hotelRoom.setHasAC(roomDto.getHasAC());
        hotelRoom.setHasBalcony(roomDto.getHasBalcony());
        hotelRoom.setHasRefridge(roomDto.getHasRefridge());
        hotelRoom.setHasTV(roomDto.getHasTV());
        hotelRoom.setHasWifi(roomDto.getHasWifi());

        hotelRoom.setRoomStatus("AVAILABLE");
        hotelRoom.setCreatedAt(ZonedDateTime.now());
        return hotelRoom;
    }

}
