package com.fyp.hotel.service.vendor.hotel.Impl;

import com.fyp.hotel.dao.hotel.HotelDAO;
import com.fyp.hotel.dto.hotel.HotelDto;
import com.fyp.hotel.dto.vendorDto.VendorDto;
import com.fyp.hotel.model.Hotel;
import com.fyp.hotel.model.Role;
import com.fyp.hotel.model.User;
import com.fyp.hotel.repository.HotelRepository;
import com.fyp.hotel.repository.RoleRepository;
import com.fyp.hotel.repository.UserRepository;
import com.fyp.hotel.service.vendor.hotel.VendorHotelService;
import com.fyp.hotel.util.FileUploaderHelper;
import com.fyp.hotel.util.ValueMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class VendorHotelServiceImpl implements VendorHotelService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final HotelRepository hotelRepository;
    private final FileUploaderHelper fileUploaderHelper;
    private final PasswordEncoder passwordEncoder;
    private final ValueMapper valueMapper;
    private final HotelDAO hotelDAO;

    @Override
    @Transactional(readOnly = true)
    public Boolean checkHotelPanExist(String hotelPan) {
        if (Boolean.TRUE.equals(hotelDAO.existByHotelPan(hotelPan))) {
            return true;
        }
        return false;
    }

    @Override
    @Transactional(readOnly = true)
    public Hotel getHotelDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User vendor = userRepository.findByUserName(username);
        return hotelRepository.findByUser(vendor);
    }

    @Override
    @Transactional
    public String updateHotelDetails(String hotelName,
                                     String hotelAddress,
                                     String hotelContact,
                                     String hotelEmail,
                                     String hotelPan,
                                     String hotelDescription,
                                     int hotelStar,
                                     MultipartFile hotelImage)
    {
        Hotel hotel = getCurrentVendorHotel();
        if (hotelImage != null && fileUploaderHelper.fileUploader(hotelImage)) {
            hotel.setHotelImage(hotelImage.getOriginalFilename());
        }

        updateHotelFields(hotel, hotelName, hotelAddress, hotelContact,
                hotelEmail, hotelPan, hotelDescription, hotelStar);

        hotel.setUpdatedAt(Instant.now());
        hotelRepository.save(hotel);

        return "Hotel details updated successfully";
    }

    private Hotel getCurrentVendorHotel() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User vendor = userRepository.findByUserName(username);
        return hotelRepository.findByUser(vendor);
    }

    private void updateHotelFields(Hotel hotel, String name, String address, String contact,
                                   String email, String pan, String description, int star) {
        if (name != null) hotel.setHotelName(name);
        if (address != null) hotel.setHotelAddress(address);
        if (contact != null) hotel.setHotelContact(contact);
        if (email != null) hotel.setHotelEmail(email);
        if (pan != null) hotel.setHotelPan(pan);
        if (description != null) hotel.setHotelDescription(description);
        if (star != 0) hotel.setHotelStar(star);
    }
}