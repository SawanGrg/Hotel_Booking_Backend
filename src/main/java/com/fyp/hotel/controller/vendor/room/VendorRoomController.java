package com.fyp.hotel.controller.vendor.room;

import com.fyp.hotel.dto.common.ApiResponse;
import com.fyp.hotel.dto.room.RoomDto;
import com.fyp.hotel.model.HotelRoom;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fyp.hotel.service.vendor.VendorServiceFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/v1/vendor")
public class VendorRoomController {

    @Autowired
    private VendorServiceFacade vendorServiceFacade;
    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping("/addHotelRooms")
    public ResponseEntity<?> addHotelRooms(
            @RequestParam("roomData") String roomDataJson,
            @RequestParam("mainRoomImage") MultipartFile mainRoomImage,
            @RequestParam(value = "roomImage1", required = false) MultipartFile roomImage1,
            @RequestParam(value = "roomImage2", required = false) MultipartFile roomImage2,
            @RequestParam(value = "roomImage3", required = false) MultipartFile roomImage3
    ) throws Exception {
        RoomDto roomDto = objectMapper.readValue(roomDataJson, RoomDto.class);
        if (vendorServiceFacade.getVendorRoomService().checkRoomExistsOrNot(roomDto.getRoomNumber())) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(400, "Room exists", null));
        }
        String status = vendorServiceFacade.getVendorRoomService().addRooms(roomDto, mainRoomImage, roomImage1, roomImage2, roomImage3);
        return ResponseEntity.ok(new ApiResponse<>(200, "Success", status));
    }

    @GetMapping("/hotelRooms")
    public ResponseEntity<Page<HotelRoom>> getHotelRooms(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(vendorServiceFacade.getVendorRoomService().getHotelRooms(page, size));
    }

    @PostMapping("/updateRoom/{roomId}")
    public ResponseEntity<?> updateRoom(
            @PathVariable Long roomId,
            @RequestParam("roomData") String roomDataJson,
            @RequestParam("mainRoomImage") MultipartFile mainRoomImage,
            @RequestParam(required = false) MultipartFile roomImage1,
            @RequestParam(required = false) MultipartFile roomImage2,
            @RequestParam(required = false) MultipartFile roomImage3
    ) throws Exception {
        RoomDto roomDto = objectMapper.readValue(roomDataJson, RoomDto.class);
        String status = vendorServiceFacade.getVendorRoomService().updateRoom(roomId, roomDto, mainRoomImage, roomImage1, roomImage2, roomImage3);
        return ResponseEntity.ok(new ApiResponse<>(200, "Success", status));
    }

    @PostMapping("/delete/{roomId}")
    public ResponseEntity<?> deleteRoom(@PathVariable Long roomId) {
        String status = vendorServiceFacade.getVendorRoomService().deleteSpecificRoom(roomId);
        return ResponseEntity.ok(new ApiResponse<>(200, "Success", status));
    }

    @GetMapping("/viewRoom/{roomId}")
    public ResponseEntity<?> viewRoom(@PathVariable Long roomId) {
        return ResponseEntity.ok(new ApiResponse<>(200, "Success", vendorServiceFacade.getVendorRoomService().getRoomDetails(roomId)));
    }

    @GetMapping("/roomHistory/{roomId}")
    public ResponseEntity<?> getRoomHistory(@PathVariable long roomId) {
        return ResponseEntity.ok(new ApiResponse<>(200, "Success", vendorServiceFacade.getVendorBookingService().getRoomHistory(roomId)));
    }
}