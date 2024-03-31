package com.fyp.hotel.serviceImpl.user;

import java.time.Instant;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import com.fyp.hotel.dao.HotelDAO;
import com.fyp.hotel.dao.HotelRoomDAO;
import com.fyp.hotel.dao.PaymentMethodDAO;
import com.fyp.hotel.dao.UserHibernateRepo;
import com.fyp.hotel.dao.user.BlogDAO;
import com.fyp.hotel.dto.BookingDTO;
import com.fyp.hotel.dto.CheckRoomAvailabilityDto;
import com.fyp.hotel.dto.DisplayHotelWithAmenitiesDto;
import com.fyp.hotel.dto.userDto.*;
import com.fyp.hotel.model.*;
import com.fyp.hotel.repository.*;
import com.fyp.hotel.util.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
@AllArgsConstructor
@NoArgsConstructor
public class UserServiceImplementation implements UserService, UserDetailsService {

    private UserRepository userRepo;
    private RoleRepository roleRepo;
    private FileUploaderHelper fileUploaderHelper;
    private PasswordEncoder passwordEncoder;
    private HotelRepository hotelRepo;
    private UserHibernateRepo userHibernateRepo;
    private HotelRoomRepository hotelRoomRepository;
    private RoomImageRepository roomImageRepository;
    private BookingRepository bookingRepository;
    private PaymentMethodDAO paymentMethodDAO;
    private OtpMailSender otpMailSender;
    private EmailSenderService emailSenderService;
    private OtpGenerator otpGenerator;
    private DaysChecker daysChecker;
    private HotelRoomDAO hotelRoomDAO;
    private HotelDAO hotelDAO;
    private PaymentRepository paymentRepository;
    private ReviewRepository hotelReviewRepository;
    private BlogRepository blogRepository;
    private BlogDAO blogDAO;
    private BlogCommentRepository blogCommentRepository;

    @Autowired
    public UserServiceImplementation(
            @Qualifier("userRepository") @Lazy UserRepository userRepo,
            @Qualifier("roleRepository") @Lazy RoleRepository roleRepo,
            @Lazy FileUploaderHelper fileUploaderHelper,
            @Lazy PasswordEncoder passwordEncoder,
            @Lazy HotelRepository hotelRepo,
            @Lazy UserHibernateRepo userHibernateRepo,
            @Lazy HotelRoomRepository hotelRoomRepository,
            @Lazy RoomImageRepository roomImageRepository,
            @Lazy BookingRepository bookingRepository,
            @Lazy PaymentMethodDAO paymentMethodDAO,
            @Lazy OtpMailSender otpMailSender,
            @Lazy EmailSenderService emailSenderService,
            @Lazy OtpGenerator otpGenerator,
            @Lazy DaysChecker daysChecker,
            @Lazy HotelRoomDAO hotelRoomDAO,
            @Lazy HotelDAO hotelDAO,
            @Lazy PaymentRepository paymentRepository,
            @Lazy ReviewRepository hotelReviewRepository,
            @Lazy BlogRepository blogRepository,
            @Lazy BlogDAO blogDAO,
            @Lazy BlogCommentRepository blogCommentRepository
    ) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.fileUploaderHelper = fileUploaderHelper;
        this.passwordEncoder = passwordEncoder;
        this.hotelRepo = hotelRepo;
        this.userHibernateRepo = userHibernateRepo;
        this.hotelRoomRepository = hotelRoomRepository;
        this.roomImageRepository = roomImageRepository;
        this.bookingRepository = bookingRepository;
        this.paymentMethodDAO = paymentMethodDAO;
        this.otpMailSender = otpMailSender;
        this.emailSenderService = emailSenderService;
        this.otpGenerator = otpGenerator;
        this.daysChecker = daysChecker;
        this.hotelRoomDAO = hotelRoomDAO;
        this.hotelDAO = hotelDAO;
        this.paymentRepository = paymentRepository;
        this.hotelReviewRepository = hotelReviewRepository;
        this.blogRepository = blogRepository;
        this.blogDAO = blogDAO;
        this.blogCommentRepository = blogCommentRepository;
    }

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

    //user view all the hotels with amenities
    @Transactional
    public List<DisplayHotelWithAmenitiesDto> getAllHotelsWithAmenities(String hotelName, String hotelLocation, int page, int size) {
        return this.hotelDAO.getHotelWithAmenitiesAndRating(hotelName, hotelLocation);
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
        System.out.println("step 1 in the service implementation");
        try {
            // Check if userProfilePicture is provided and save it
            System.out.println("step 2 in the service implementation");
            boolean isProfilePictureProvided = false;
            if (userProfilePicture != null && !userProfilePicture.isEmpty()) {
                System.out.println("step 4 in the service implementation");
                isProfilePictureProvided = fileUploaderHelper.fileUploader(userProfilePicture);
                System.out.println("step 5 in the service implementation");
            }
            System.out.println("step 6 in the service implementation");
            // Retrieve authenticated user
            Authentication authenticatedUser = SecurityContextHolder.getContext().getAuthentication();
            if (authenticatedUser != null) {
                String authenticatedUserName = authenticatedUser.getName();
                User user = userRepo.findByUserName(authenticatedUserName);

                // Update user details
                if (userName != null && !userName.isEmpty()) {
                    user.setUserName(userName);
                }
                if (userFirstName != null && !userFirstName.isEmpty()) {
                    user.setUserFirstName(userFirstName);
                }
                if (userLastName != null && !userLastName.isEmpty()) {
                    user.setUserLastName(userLastName);
                }
                if (userEmail != null && !userEmail.isEmpty()) {
                    user.setUserEmail(userEmail);
                }
                if (userPhone != null && !userPhone.isEmpty()) {
                    user.setUserPhone(userPhone);
                }
                if (userAddress != null && !userAddress.isEmpty()) {
                    user.setUserAddress(userAddress);
                }
                if (dateOfBirth != null && !dateOfBirth.isEmpty()) {
                    user.setDateOfBirth(dateOfBirth);
                }
                if (isProfilePictureProvided) {
                    user.setUserProfilePicture("/images/" + userProfilePicture.getOriginalFilename());
                }

                user.setUpdatedAt(Instant.now());
                userRepo.save(user);

                return "User details updated successfully";
            } else {
                return "User not authenticated";
            }
        } catch (DataAccessException e) {
            return "Database error: " + e.getMessage();
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

        //extracting if the hotel room is booked or not


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
            booking.setStatus(Status.PENDING);

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

    @Transactional
    public List<Hotel> getHotelBasedOnNameOrLocation(
            String hotelName,
            String hotelLocation,
            int page,
            int size
    ){
        return hotelDAO.getHotelsBasedOnCondition(hotelName, hotelLocation, page, size);
    }

    @Transactional
    public CheckRoomAvailabilityDto checkRoomAvailability(
            Long roomId
    ){
        //checking first in the booking table
        List<Booking> bookings = hotelDAO.getBookingsForToday(roomId);

        //checking in the khalti payment table

        //adding in this condition
        if(bookings.size() > 0){
            CheckRoomAvailabilityDto checkRoomAvailabilityDto = new CheckRoomAvailabilityDto();

            checkRoomAvailabilityDto.setRoomId(roomId);
            checkRoomAvailabilityDto.setStatus("Booked");
            checkRoomAvailabilityDto.setUserName(bookings.get(0).getUser().getUsername());
            checkRoomAvailabilityDto.setCheckInDate(bookings.get(0).getCheckInDate().toString());
            checkRoomAvailabilityDto.setCheckOutDate(bookings.get(0).getCheckOutDate().toString());

            return checkRoomAvailabilityDto;
        }else{
            CheckRoomAvailabilityDto checkRoomAvailabilityDto = new CheckRoomAvailabilityDto();

            checkRoomAvailabilityDto.setRoomId(roomId);
            checkRoomAvailabilityDto.setStatus("Available");

            return checkRoomAvailabilityDto;
        }
    }

    //post user review at hotel
    public String postHotelReview(
            long hotelId,
            HotelReviewDTO hotelReviewDTO
    ) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userName = authentication.getName();

            User user = userRepo.findByUserName(userName);

            Hotel hotel = hotelRepo.findById(hotelId)
                    .orElseThrow(() -> new RuntimeException("Hotel not found with id: " + hotelId));

            Review review = new Review();

            review.setReviewContent(hotelReviewDTO.getHotelReview());
            review.setHotel(hotel);
            review.setUser(user);
            review.setCreatedDate(Instant.now().toString());

            // Save the review
            hotelReviewRepository.save(review);

            return "Review posted successfully.";
        } catch (Exception e) {
            e.printStackTrace(); // Handle the exception appropriately
            return "Failed to post review.";
        }
    }

    //extract all the review of the specific hotel
    @Transactional
    public List<HotelReviewDTO> getAllHotelReviews(Long hotelId) {
        List<Review> reviews = hotelReviewRepository.findByHotelId(hotelId);

        return reviews.stream()
                .map(review -> new HotelReviewDTO(
                        review.getHotel().getHotelId(),
                        review.getReviewContent(),
                        review.getUser().getUsername(),
                        review.getCreatedDate()
                ))
                .toList();
    }

    //post blog by user
    @Transactional
    public String postUserBlog(MultipartFile blogImage, BlogDTO blogDTO) {
        try {
            boolean isImageUploaded = fileUploaderHelper.fileUploader(blogImage);
            if (!isImageUploaded) {
                return "Failed to upload blog image.";
            }

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userName = authentication.getName();

            User user = userRepo.findByUserName(userName);
            if (user == null) {
                return "User not found.";
            }

            Blog blog = createBlogFromDTO(blogDTO, user, blogImage);
            blogRepository.save(blog);

            return "Blog posted successfully.";
        } catch (Exception e) {
            e.printStackTrace(); // Handle the exception appropriately
            return "Failed to post blog.";
        }
    }

    private Blog createBlogFromDTO(BlogDTO blogDTO, User user, MultipartFile blogImage) {
        Blog blog = new Blog();
        blog.setTitle(blogDTO.getTitle());
        blog.setDescription(blogDTO.getDescription());
        blog.setCreatedDate(blogDTO.getCreatedDate());
        blog.setUser(user);
        blog.setStatus("PENDING");
        blog.setBlogTag(blogDTO.getBlogTag());
        blog.setBlogImage("/images/" + blogImage.getOriginalFilename());
        return blog;
    }


    @Transactional
    public List<BlogDTO> viewUserBlog() {
        return blogRepository.findAll().stream()
                .filter(blog -> "VERIFIED".equals(blog.getStatus()))
                .map(blog -> new BlogDTO(
                        blog.getBlogId(),
                        blog.getTitle(),
                        blog.getDescription(),
                        blog.getBlogTag(),
                        blog.getBlogImage(),
                        blog.getStatus(),
                        blog.getCreatedDate(),
                        blog.getUser()
                ))
                .toList();
    }

    @Transactional
    public BlogDTO getSpecificBlog(long blogId) {
        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> new RuntimeException("Blog not found with id: " + blogId));

        return new BlogDTO(
                blog.getBlogId(),
                blog.getTitle(),
                blog.getDescription(),
                blog.getBlogTag(),
                blog.getBlogImage(),
                blog.getStatus(),
                blog.getCreatedDate(),
                blog.getUser()
        );
    }

    @Transactional
    public String postBlogComment(long blogId, BlogCommentDTO blogCommentDTO){
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userName = authentication.getName();

            User user = userRepo.findByUserName(userName);

            Blog blog = blogRepository.findById(blogId)
                    .orElseThrow(() -> new RuntimeException("Blog not found with id: " + blogId));

            BlogComment blogComment = new BlogComment();

            blogComment.setComment(blogCommentDTO.getComment());
            blogComment.setStatus("VERIFIED");
            blogComment.setBlog(blog);
            blogComment.setUser(user);
            blogComment.setCreatedDate(LocalDate.now());


            blogCommentRepository.save(blogComment);

            return "Comment posted successfully.";
        } catch (Exception e) {
            e.printStackTrace(); // Handle the exception appropriately
            return "Failed to post comment.";
        }
    }

    @Transactional
    public List<BlogCommentDTO> getAllBlogCommentSpecificBlog(long blogId){
        List<BlogComment> blogComments = this.blogCommentRepository.findAllByBlogId(blogId);

        return blogComments.stream()
                .map(blogComment -> new BlogCommentDTO(
                        blogComment.getCommentId(),
                        blogComment.getComment(),
                        blogComment.getUser(),
                        blogComment.getCreatedDate().toString()
                ))
                .collect(Collectors.toList()); // Collect the mapped BlogCommentDTO objects into a list
    }

    //get all the booking details of the specific user
    @Transactional
    public List<BookingDTO> viewBookingDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();

        User user = userRepo.findByUserName(userName);

        if (user == null) {
            // Handle the case when user is not found
            return Collections.emptyList(); // Or throw an exception
        }

        List<Booking> bookings = bookingRepository.findByUserWithHotelRoom(user.getUserId());

        return bookings.stream()
                .map(booking -> new BookingDTO(
                        booking.getBookingId(),
                        booking.getCheckInDate(),
                        booking.getCheckOutDate(),
                        booking.getBookingDate(),
                        booking.getStatus(),
                        booking.getTotalAmount(),
                        booking.getUser(),
                        booking.getHotelRoom(),
                        booking.getPaymentMethod().getPaymentMethodName(),
                        booking.getCreatedAt()
                )).toList();
    }

}
