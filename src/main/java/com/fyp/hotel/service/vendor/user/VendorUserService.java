package com.fyp.hotel.service.vendor.user;

import com.fyp.hotel.dto.hotel.HotelDto;
import com.fyp.hotel.dto.vendorDto.VendorDto;
import org.springframework.web.multipart.MultipartFile;

public interface VendorUserService {
    String registerVendor(VendorDto vendorDto, MultipartFile userImage, HotelDto hotelDto, MultipartFile hotelImage);

}
