package com.fyp.hotel.service.admin.hotel.Impl;

import com.fyp.hotel.dao.admin.AdminDAO;
import com.fyp.hotel.dto.vendorDto.HotelDto;
import com.fyp.hotel.repository.HotelRepository;
import com.fyp.hotel.service.admin.hotel.AdminHotelService;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import com.fyp.hotel.model.Hotel;
import com.fyp.hotel.repository.*;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdminHotelServiceImpl implements AdminHotelService {

    private final AdminDAO adminDAO;
    private final HotelRepository hotelRepository;

    @Autowired
    public AdminHotelServiceImpl(
            AdminDAO adminDAO,
            HotelRepository hotelRepository
    ) {
        this.adminDAO = adminDAO;
        this.hotelRepository = hotelRepository;
    }

    @Override
    public List<HotelDto> getAllHotels(int page, int size) {
        List<Hotel> hotels = adminDAO.getAllActiveHotels(page, size);
        List<HotelDto> hotelDtos = new ArrayList<>();
        for (Hotel hotel : hotels) {
            hotelDtos.add(new HotelDto(
                            hotel.getHotelId(),
                            hotel.getHotelName(),
                            hotel.getHotelAddress(),
                            hotel.getHotelContact(),
                            hotel.getHotelEmail(),
                            hotel.getHotelPan(),
                            hotel.getHotelDescription(),
                            hotel.getHotelStar(),
                            hotel.getIsDeleted(),
                            hotel.getCreatedAt().toString(),
                            hotel.getUpdatedAt().toString(),
                            hotel.getHotelImage()
                    ));
        }
        return hotelDtos;
    }

    @Override
    public HotelDto getHotelProfile(long userId) {
        Hotel hotel = hotelRepository.findByUser(adminDAO.getUserProfile(userId));
        return new HotelDto(
                hotel.getHotelId(),
                hotel.getHotelName(),
                hotel.getHotelAddress(),
                hotel.getHotelContact(),
                hotel.getHotelEmail(),
                hotel.getHotelPan(),
                hotel.getHotelDescription(),
                hotel.getHotelStar(),
                hotel.getIsDeleted(),
                hotel.getCreatedAt().toString(),
                hotel.getUpdatedAt().toString(),
                hotel.getHotelImage()
        );
    }
}
