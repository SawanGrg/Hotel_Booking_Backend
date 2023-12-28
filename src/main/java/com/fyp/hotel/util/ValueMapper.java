package com.fyp.hotel.util;

import com.fyp.hotel.dto.userDto.BookDto;
import com.fyp.hotel.dto.userDto.UserDto;
import com.fyp.hotel.dto.vendorDto.HotelDto;
import com.fyp.hotel.dto.vendorDto.RoomDto;
import com.fyp.hotel.dto.vendorDto.VendorDto;
import com.fyp.hotel.model.*;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class ValueMapper {

    @Autowired
    private PasswordEncoder passwordEncoder;

    // conversion of the userDto to user
    public User conversionToUser(VendorDto userDto) {
        User user = new User();
        user.setUserName(userDto.getUserName());
        user.setUserFirstName(userDto.getUserFirstName());
        user.setUserLastName(userDto.getUserLastName());
        user.setUserEmail(userDto.getUserEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setUserPhone(userDto.getUserPhone());
        user.setUserAddress(userDto.getUserAddress());
        user.setDateOfBirth(userDto.getDateOfBirth());
        user.setUserStatus("ACTIVE");
        user.setCreatedAt(Instant.now()); // instant is a class that is used to get the current time
        return user;
    }

    // conversion of the hotelDto to hotel
    public Hotel conversionToHotel(HotelDto hotelDto) {
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

    public HotelRoom conversionToHotelRoom(@NotNull  RoomDto roomDto) {

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

    // conversion of the hotelRoom to hotelRoomDto
    public List<HotelDto> mapToHotelDTOs(List<Hotel> hotels) {

        List<HotelDto> hotelDTOs = new ArrayList<>();
        for (Hotel hotel : hotels) {

            HotelDto hotelDTO = new HotelDto();

            hotelDTO.setHotelId(hotel.getHotelId());
            hotelDTO.setHotelName(hotel.getHotelName());
            hotelDTO.setHotelAddress(hotel.getHotelAddress());
            hotelDTO.setHotelContact(hotel.getHotelContact());
            hotelDTO.setHotelEmail(hotel.getHotelEmail());
            hotelDTO.setHotelPan(hotel.getHotelPan());
            hotelDTO.setHotelImage(hotel.getHotelImage());
            //i dont have the hotel description in the hotel table of the database
//            hotelDTO.setHotelDescription(hotel.getHotelDescription());

            hotelDTOs.add(hotelDTO);
        }
        return hotelDTOs;
    }

    // conversion of the hotelRoom to hotelRoomDto
    public List<RoomDto> mapToHotelRoom(List<HotelRoom> hotelRooms){
        List<RoomDto> roomDtos = new ArrayList<>();
        for(HotelRoom hotelRoom : hotelRooms){

            RoomDto roomDto = new RoomDto();

            roomDto.setRoomNumber(hotelRoom.getRoomNumber());
            roomDto.setRoomType(hotelRoom.getRoomType().toString());
            roomDto.setRoomCategory(hotelRoom.getRoomCategory().toString());
            roomDto.setRoomBed(hotelRoom.getRoomBed().toString());
            roomDto.setRoomPrice(hotelRoom.getRoomPrice());
            roomDto.setRoomDescription(hotelRoom.getRoomDescription());
            roomDto.setRoomStatus(hotelRoom.getRoomStatus());
            roomDto.setHasAC(hotelRoom.getHasAC());
            roomDto.setHasBalcony(hotelRoom.getHasBalcony());
            roomDto.setHasRefridge(hotelRoom.getHasRefridge());
            roomDto.setHasTV(hotelRoom.getHasTV());
            roomDto.setHasWifi(hotelRoom.getHasWifi());
            roomDto.setMainRoomImage(hotelRoom.getMainRoomImage());

            roomDtos.add(roomDto);
        }
        return roomDtos;
    }

    //check the incoming booking info and map it to the booking object
    public BookDto mapToBooking(
           Long roomId,
           String checkInDate,
           String checkOutDate,
           String daysOfStay,
           String paymentMethod
    ){
        BookDto bookDto = new BookDto();

        // Define a DateTimeFormatter corresponding to your date string format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        bookDto.setRoomId(roomId);
        bookDto.setCheckInDate(LocalDate.parse(checkInDate, formatter));  //convert string to local date
        bookDto.setCheckOutDate(LocalDate.parse(checkOutDate, formatter));
        bookDto.setDaysOfStay(Long.parseLong(daysOfStay)); //convert string to long
        bookDto.setBookingDate(LocalDate.now()); //get the current date
        bookDto.setPaymentMethod(paymentMethod);
        bookDto.setCreatedAt(Instant.now());

        return bookDto;
    }

}
