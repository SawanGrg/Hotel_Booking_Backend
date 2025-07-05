package com.fyp.hotel.service.vendor.user.Impl;

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
import com.fyp.hotel.service.vendor.user.VendorUserService;
import com.fyp.hotel.util.FileUploaderHelper;
import com.fyp.hotel.util.ValueMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class VendorUserServiceImpl implements VendorUserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final HotelRepository hotelRepository;
    private final FileUploaderHelper fileUploaderHelper;
    private final PasswordEncoder passwordEncoder;
    private final ValueMapper valueMapper;
    private final HotelDAO hotelDAO;
    private final VendorHotelService vendorHotelService;

    @Override
    @Transactional
    public String registerVendor(VendorDto vendorDto,
                                 MultipartFile userImage,
                                 HotelDto hotelDto,
                                 MultipartFile hotelImage)
    {
        if (!fileUploaderHelper.fileUploader(userImage) || !fileUploaderHelper.fileUploader(hotelImage)) {
            return "Vendor Registration Failed - File upload error";
        }

        if(vendorHotelService.checkHotelPanExist(hotelDto.getHotelPan())) {
            return "Vendor Registration Failed - Hotel PAN already exists";
        }

        User user = createVendorUser(vendorDto, userImage);
        Hotel hotel = createHotel(hotelDto, hotelImage, user);

        hotelRepository.save(hotel);
        return "Vendor Registered Successfully";
    }

    private User createVendorUser(VendorDto vendorDto, MultipartFile userImage) {
        User user = valueMapper.conversionToUser(vendorDto);
        user.setUserProfilePicture("/images/" + userImage.getOriginalFilename());
        user.setPassword(passwordEncoder.encode(vendorDto.getPassword()));

        Role vendorRole = roleRepository.findByRoleName("ROLE_VENDOR");
        user.getRoles().add(vendorRole);
        user.setUserStatus("PENDING");

        return userRepository.save(user);
    }

    private Hotel createHotel(HotelDto hotelDto, MultipartFile hotelImage, User user) {
        Hotel hotel = valueMapper.conversionToHotel(hotelDto);
        hotel.setHotelImage(hotelImage.getOriginalFilename());
        hotel.setIsDeleted(false);
        hotel.setUser(user);
        return hotel;
    }

}
