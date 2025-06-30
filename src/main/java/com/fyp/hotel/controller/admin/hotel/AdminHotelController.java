// AdminHotelController.java
package com.fyp.hotel.controller.admin.hotel;

import com.fyp.hotel.dto.common.ApiResponse;
import com.fyp.hotel.dto.hotel.HotelDto;
import com.fyp.hotel.dto.common.PaginationRequestDTO;
import com.fyp.hotel.service.admin.AdminServiceFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/admin")
public class AdminHotelController {

    @Autowired
    private AdminServiceFacade adminServiceFacade;

    @GetMapping("/viewAllHotels")
    public ResponseEntity<?> viewAllHotels(
            @ModelAttribute PaginationRequestDTO paginationRequestDTO
    ) {
        List<HotelDto> hotels = adminServiceFacade.hotelService.getAllHotels(paginationRequestDTO.getPageNumber(), paginationRequestDTO.getPageSize());
        return ResponseEntity.ok(new ApiResponse<>(200, "Success", hotels));
    }

    @GetMapping("/getSpecificHotel/{userId}")
    public ResponseEntity<?> getSpecificHotel(@PathVariable long userId) {
        HotelDto hotel = adminServiceFacade.hotelService.getHotelProfile(userId);
        return ResponseEntity.ok(new ApiResponse<>(200, "Success", hotel));
    }
}