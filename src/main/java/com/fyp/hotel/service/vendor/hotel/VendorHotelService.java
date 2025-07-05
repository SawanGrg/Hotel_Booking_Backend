package com.fyp.hotel.service.vendor.hotel;

import com.fyp.hotel.model.Hotel;
import org.springframework.web.multipart.MultipartFile;

public interface VendorHotelService {
    Boolean checkHotelPanExist(String hotelPan);
    Hotel getHotelDetails();
    String updateHotelDetails(String hotelName, String hotelAddress, String hotelContact,
                              String hotelEmail, String hotelPan, String hotelDescription,
                              int hotelStar, MultipartFile hotelImage);
}