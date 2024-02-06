package com.fyp.hotel.serviceImpl.user;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.fyp.hotel.dao.HotelRoomDAO;
import com.fyp.hotel.dao.PaymentMethodDAO;
import com.fyp.hotel.dao.UserHibernateRepo;
import com.fyp.hotel.dto.userDto.BookDto;
import com.fyp.hotel.dto.userDto.UserChangePasswordDto;
import com.fyp.hotel.model.*;
import com.fyp.hotel.repository.*;
import com.fyp.hotel.util.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataAccessException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.multipart.MultipartFile;

import com.fyp.hotel.service.user.UserService;

import jakarta.transaction.Transactional;

@Service
public class UserServiceImplementation implements UserService, UserDetailsService {

    @Autowired
    @Lazy
    private UserRepository userRepo;
    @Autowired
    @Lazy
    private RoleRepository roleRepo;
    @Autowired
    @Lazy
    private FileUploaderHelper fileUploaderHelper;
    @Autowired
    @Lazy
    private PasswordEncoder passwordEncoder;
    @Autowired
    @Lazy
    private HotelRepository hotelRepo;
    @Autowired
    @Lazy
    private UserHibernateRepo userHibernateRepo;
    @Autowired
    @Lazy
    private HotelRoomRepository hotelRoomRepository;
    @Autowired
    @Lazy
    private RoomImageRepository roomImageRepository;
    @Autowired
    @Lazy
    private BookingRepository bookingRepository;
    @Autowired
    @Lazy
    private PaymentMethodDAO paymentMethodDAO;
    @Autowired
    @Lazy
    private OtpMailSender otpMailSender;
    @Autowired
    @Lazy
    private EmailSenderService emailSenderService;
    @Autowired
    @Lazy
    private OtpGenerator otpGenerator;
    @Autowired
    @Lazy
    private DaysChecker daysChecker;
    @Autowired
    @Lazy
    private HotelRoomDAO hotelRoomDAO;


    private Map<String, String> storeOtpAndUserName = new ConcurrentHashMap<>();

//    @Scheduled(cron = "10 * * * * *") // Cron expression 10 seconds
//    public void execute() {
//        System.out.println("Cron job running every minute");
//    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userRepo.findByUserName(username);
        // Extract roles of a user based on the user details
        Set<Role> roles = this.roleRepo.findRolesByUser(user); // Corrected method name
        user.setRoles(roles);
        if (!user.isEnabled()) {
            throw new DisabledException("User is disabled");
        }
        return user;
    }

    @Transactional
    public User getUserWithRoles(String username) {
        User user = userRepo.findByUserName(username);
        // Accessing roles should work within the transaction.
        user.getRoles().size(); // This fetches the roles from the database.

        return user;
    }

    @Transactional
    public String registerUser(User user, MultipartFile userImage) {
        try {
            System.out.println(" this is the user password before encoding " + user.getPassword());

            String encodedPassword = passwordEncoder.encode(user.getPassword());

            System.out.println(" this is the user password after encoding " + encodedPassword);
            user.setPassword(encodedPassword);
            user.setCreatedAt(Instant.now());
            boolean isImageUploaded = fileUploaderHelper.fileUploader(userImage);
            String imageUrl = isImageUploaded ? "/images/" + userImage.getOriginalFilename() : "/images/default.png";
            user.setUserProfilePicture(imageUrl);
            user.setUserStatus("ACTIVE");
            Role defaultRole = roleRepo.findByRoleName("ROLE_USER");
            if (defaultRole != null) {
                user.getRoles().add(defaultRole);
            }

            userRepo.save(user);

            //generate otp
            String otp = otpGenerator.generateOTP();
            //store in map
            storeOtpAndUserName.put(otp, user.getUsername());
            //pass otp and set mail message
            Map<String, String> sendMail = otpMailSender.getVerifyEmailMessage(otp);
            //send mail to the specific user
            emailSenderService.sendEmail(
                    user.getUserEmail(),
                    sendMail.get("subject"),
                    sendMail.get("message"));

            return "User registered successfully";
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    //verify otp
    @Transactional
    public String verifyOtp(String otp) {
        try {
              String userName = storeOtpAndUserName.get(otp);
              System.out.println("user name from map: " + userName);
              System.out.println("otp from map: " + otp);
            User user = userRepo.findByUserName(userName);
            user.setUserStatus("VERIFIED");
            userRepo.save(user);
            return "verified successfully";
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }
    //extract all the hotels details with pagination
    @Transactional
    public List<Hotel> getAllHotels(int page, int size) {
        return this.hotelRepo.findAll();
    }

    //update user profile
    @Transactional
    public String updateDetails(
            String userName,
            String userFirstName,
            String userLastName,
            String userEmail,
            String userPhone,
            String userAddress,
            String dateOfBirth,
            MultipartFile userProfilePicture
    ) {
        try {

            if(userProfilePicture != null){
                boolean isImageUploaded = fileUploaderHelper.fileUploader(userProfilePicture);
            }

            Authentication authenticatedUser = SecurityContextHolder.getContext().getAuthentication();
            if (authenticatedUser != null) {


                String authenticatedUserName = authenticatedUser.getName();
                User user = userRepo.findByUserName(authenticatedUserName);


                user.setUserName(userName.isEmpty() ? user.getUsername() : userName);
                user.setUserFirstName(userFirstName.isEmpty() ? user.getUserFirstName() : userFirstName);
                user.setUserLastName(userLastName.isEmpty() ? user.getUserLastName() : userLastName);
                user.setUserEmail(userEmail.isEmpty() ? user.getUserEmail() : userEmail);
                user.setUserPhone(userPhone.isEmpty() ? user.getUserPhone() : userPhone);
                user.setUserAddress(userAddress.isEmpty() ? user.getUserAddress() : userAddress);
                user.setDateOfBirth(dateOfBirth.isEmpty() ? user.getDateOfBirth() : dateOfBirth);
                user.setUserProfilePicture(userProfilePicture.isEmpty() ? user.getUserProfilePicture() : "/images/" + userProfilePicture.getOriginalFilename());
                user.setUpdatedAt(Instant.now());

                userRepo.save(user);
                return "User details updated successfully";
            } else {
                return "User not authenticated"; // Handle unauthenticated user
            }
        } catch (DataAccessException e) {
            return "Database error: " + e.getMessage(); // Handle database errors
        } catch (Exception e) {
            return "An error occurred: " + e.getMessage();
        }
    }

    //change user password
    @Transactional
    public String changePassword(
            UserChangePasswordDto userChangePasswordDto
    ){
            try {
            Authentication authenticatedUser = SecurityContextHolder.getContext().getAuthentication();
            if (authenticatedUser != null) {
                String authenticatedUserName = authenticatedUser.getName();

                User user = userRepo.findByUserName(authenticatedUserName);

                String oldPassword = userChangePasswordDto.getOldPassword();
                String newPassword = userChangePasswordDto.getNewPassword();

                if (passwordEncoder.matches(oldPassword, user.getPassword())) {
                    user.setPassword(passwordEncoder.encode(newPassword));
                    user.setUpdatedAt(Instant.now());
                    userRepo.save(user);
                    return "Password changed successfully";
                } else {
                    return "Old password is incorrect";
                }
            } else {
                return "User not authenticated"; // Handle unauthenticated user
            }
        } catch (DataAccessException e) {
            return "Database error: " + e.getMessage(); // Handle database errors
        } catch (Exception e) {
            return "An error occurred: " + e.getMessage();
        }
    }

    //logout user logic
    @Transactional
    public String clearOutJwtToken(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            //setAuthenticated(false) will clear the context
            authentication.setAuthenticated(false);
            //clear the context holder
            SecurityContextHolder.clearContext();
            return "User logged out successfully";
        }
        else {
            return "User not authenticated";
        }
    }

    //Get all the details of rooms of a specific hotel
//    @Transactional
//    public List<HotelRoom> getAllRoomsDetails(Long hotelId) {
//        return this.hotelRepo.findHotelRoomByHotel_HotelId(hotelId);
//    }

    @Transactional
    public List<HotelRoom> getAllRoomsOfHotel(Long hotelId, int page, int size) {
        List<HotelRoom> roomDatas = this.hotelRoomRepository.findHotelRoomByHotel_HotelId(hotelId);
        List< RoomImage> roomImages = this.roomImageRepository.findByHotelRoom_RoomId(hotelId);

        for (HotelRoom roomData : roomDatas) {
            for (RoomImage roomImage : roomImages) {
                if (roomData.getRoomId() == roomImage.getHotelRoom().getRoomId()) {
                    roomData.getRoomImages().add(roomImage);
                }
            }
        }
        return roomDatas;
    }

    public User getUserById() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println("from user service implementation " + name);
        return userHibernateRepo.getUserByUsername(name);
    }

    @Transactional
    public String hotelPaymentGateWay(
            @Validated BookDto bookDto
    ){
        if(bookDto.getPaymentMethod().equals("Cash")){

            Authentication authenticatedUser = SecurityContextHolder.getContext().getAuthentication();
            String userName = authenticatedUser.getName();

            User user = userRepo.findByUserName(userName);
            Long userId = user.getUserId();
            System.out.println("user id: " + userId);

            HotelRoom hotelRoom = hotelRoomRepository.findByRoomId(bookDto.getRoomId());
            Long roomId = hotelRoom.getRoomId();
            Double roomPrice = hotelRoom.getRoomPrice();
            PaymentMethod paymentMethodObject = paymentMethodDAO.getPaymentMethod(bookDto.getPaymentMethod());

            long daysOfStay = daysChecker.getDays(bookDto.getCheckInDate(), bookDto.getCheckOutDate());
            long totalAmount = Long.valueOf(roomPrice.intValue() * daysOfStay);

            System.out.println("days of stay: " + daysOfStay);
            System.out.println("total amount: " + totalAmount);

            Booking booking = new Booking();

            booking.setHotelRoom(hotelRoom);
            booking.setUser(user);
            booking.setPaymentMethod(paymentMethodObject);
            booking.setCheckInDate(bookDto.getCheckInDate());
            booking.setCheckOutDate(bookDto.getCheckOutDate());
            booking.setBookingDate(bookDto.getBookingDate());
            booking.setNumberOfGuest(bookDto.getNumberOfGuest());
            booking.setTotalAmount(totalAmount);
            booking.setCreatedAt(Instant.now());

            bookingRepository.save(booking);

            return "Payment successful by cash on arrival";
        }
        if(bookDto.getPaymentMethod().equals("KHALTI")){
            return "Payment successful by khalti";
        }
        return "Payment failed";
    }

    public List<HotelRoom> filterRooms(
            Long hotelId,
            String roomType,
            String roomCategory,
            String roomBed,
            String minRoomPrice,
            String maxRoomPrice,
            Boolean hasAC,
            Boolean hasBalcony,
            Boolean hasRefridge,
            int page,
            int size
    ){
        System.out.println("in the service implementation");
        // Delegate to the DAO for retrieving filtered hotel rooms
        return hotelRoomDAO.getHotelRooms(
                hotelId,
                roomType,
                roomCategory,
                roomBed,
                minRoomPrice,
                maxRoomPrice,
                hasAC,
                hasBalcony,
                hasRefridge,
                page,
                size
        );
    }
}
