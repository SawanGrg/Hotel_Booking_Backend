package com.fyp.hotel.serviceImpl.vendor;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.*;

import com.fyp.hotel.dao.booking.BookingDAO;
import com.fyp.hotel.dao.hotel.HotelDAO;
import com.fyp.hotel.dto.userDto.BookingStatusDTO;
import com.fyp.hotel.dto.vendorDto.*;
import com.fyp.hotel.model.*;
import com.fyp.hotel.repository.*;
import com.fyp.hotel.util.EmailSenderService;
import com.fyp.hotel.util.ValueMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fyp.hotel.service.vendor.VendorService;
import com.fyp.hotel.util.FileUploaderHelper;

import jakarta.transaction.Transactional;

@Service
public class VendorServiceImplementation implements VendorService {

    @Autowired
    @Lazy
    private UserRepository userRepo;
    @Autowired
    @Lazy
    private RoleRepository roleRepo;
    @Autowired
    @Lazy
    private HotelRepository hotelRepo;
    @Autowired
    @Lazy
    private HotelRoomRepository hotelRoomRepo;
    @Autowired
    @Lazy
    RoomImageRepository roomImageRepo;
    @Autowired
    @Lazy
    private FileUploaderHelper fileUploaderHelper;
    @Autowired
    @Lazy
    private PasswordEncoder passwordEncoder;
    @Autowired
    @Lazy
    private ValueMapper valueMapper;
    @Autowired
    @Lazy
    private RoomImageRepository roomImageRepository;
    @Autowired
    @Lazy
    private ReportRepository reportRepository;
    @Autowired
    @Lazy
    private HotelDAO hotelDAO;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    @Lazy
    private EmailSenderService emailSenderService;
    @Lazy
    @Autowired
    private BookingDAO bookingDAO;
    @Lazy
    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    @Transactional
    public Boolean checkHotelPanExist(String hotelPan) {
        if (hotelDAO.existByHotelPan(hotelPan)) {
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public String registerVendor(VendorDto vendorDto, MultipartFile userImage, HotelDto hotelDto,
                                 MultipartFile hotelImage) {

        User userObj = valueMapper.conversionToUser(vendorDto);
        Hotel hotelObj = valueMapper.conversionToHotel(hotelDto);

        boolean isUserImageUploaded = fileUploaderHelper.fileUploader(userImage);
        boolean isHotelImageUploaded = fileUploaderHelper.fileUploader(hotelImage);

        if (isUserImageUploaded && isHotelImageUploaded) {

            userObj.setUserProfilePicture("/images/" + userImage.getOriginalFilename());
            hotelObj.setHotelImage(hotelImage.getOriginalFilename());
            hotelObj.setIsDeleted(false);

            Role vendorRole = roleRepo.findByRoleName("ROLE_VENDOR");
            userObj.getRoles().add(vendorRole);
            userObj.setUserStatus("PENDING");

            User currentUser = userRepo.save(userObj);

            if (currentUser == null) {
                return "Vendor Registration Failed";
            }

            hotelObj.setUser(currentUser);
            hotelRepo.save(hotelObj);

            return "Vendor Registered Successfully";
        } else {
            return "Vendor Registration Failed";
        }
    }

    @Override
    @Transactional
    public String addRooms(RoomDto roomData, MultipartFile mainRoomImage, MultipartFile roomImage1,
                           MultipartFile roomImage2, MultipartFile roomImage3) {

        System.out.println("room data : from vendor service " + roomData);
        System.out.println("main room image : from vendor service " + mainRoomImage.getOriginalFilename());
//        System.out.println("room image 1 : " + roomImage1.getOriginalFilename());
//        System.out.println("room image 2 : " + roomImage2.getOriginalFilename());
//        System.out.println("room image 3 : " + roomImage3.getOriginalFilename());

        System.out.println("room data : step 1");
        HotelRoom hotelRoom = valueMapper.conversionToHotelRoom(roomData);
        System.out.println("room data : " + hotelRoom);
        System.out.println("room data : step 2");

        System.out.println("hotel room --> : " + hotelRoom);

        boolean isMainRoomImageUploaded = fileUploaderHelper.fileUploader(mainRoomImage);
        System.out.println("room data : step 3");

        if (isMainRoomImageUploaded) {

            hotelRoom.setMainRoomImage(mainRoomImage.getOriginalFilename());

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            String username = authentication.getName();
            System.out.println("username from vendor service : " + username);

            if (authentication.isAuthenticated()) {
                System.out.println("authentication is not null and is : " + authentication.getPrincipal());
                Hotel hotel = hotelRepo.findByUser(userRepo.findByUserName(username));
                System.out.println("hotel from vendor service before setting the hotel id: " + hotel);
                hotelRoom.setHotel(hotel);
            }
            //marking the hotel room as not deleted
            hotelRoom.setIsDeleted(false);

            HotelRoom currentRoom = hotelRoomRepo.save(hotelRoom);
            System.out.println("hotel from vendor service after setting the hotel id: ");
            if (currentRoom == null) {
                return "Room Registration Failed";
            }

            if (roomImage1 != null) {
                boolean isRoomImage1Uploaded = fileUploaderHelper.fileUploader(roomImage1);
                if (isRoomImage1Uploaded) {
                    RoomImage roomImageObj = new RoomImage();
                    roomImageObj.setImageUrl(roomImage1.getOriginalFilename());
                    roomImageObj.setHotelRoom(currentRoom);
                    roomImageObj.setCreatedAt(ZonedDateTime.now());
                    roomImageObj.setImageStatus("ACTIVE");
                    roomImageRepo.save(roomImageObj);
                }
            }

            if (roomImage2 != null) {
                boolean isRoomImage2Uploaded = fileUploaderHelper.fileUploader(roomImage2);
                if (isRoomImage2Uploaded) {
                    RoomImage roomImageObj = new RoomImage();
                    roomImageObj.setImageUrl(roomImage2.getOriginalFilename());
                    roomImageObj.setHotelRoom(currentRoom);
                    roomImageObj.setCreatedAt(ZonedDateTime.now());
                    roomImageObj.setImageStatus("ACTIVE");
                    roomImageRepo.save(roomImageObj);
                }
            }

            if (roomImage3 != null) {
                boolean isRoomImage3Uploaded = fileUploaderHelper.fileUploader(roomImage3);
                if (isRoomImage3Uploaded) {
                    RoomImage roomImageObj = new RoomImage();
                    roomImageObj.setImageUrl(roomImage3.getOriginalFilename());
                    roomImageObj.setHotelRoom(currentRoom);
                    roomImageObj.setCreatedAt(ZonedDateTime.now());
                    roomImageObj.setImageStatus("ACTIVE");
                    roomImageRepo.save(roomImageObj);
                }
            }
        }
        return "Room Registered Successfully";
    }

    //extract all the hotel rooms and their images with pagination
    @Transactional
    public Page<HotelRoom> getHotelRooms(int page, int size) {
        Authentication authenticatedUser = SecurityContextHolder.getContext().getAuthentication();
        String username = authenticatedUser.getName();

        // Fetch the hotel ID for the given username
        Long hotelId = hotelRepo.findByUser(userRepo.findByUserName(username)).getHotelId();

        // Fetch the hotel rooms with pagination
        Pageable pageable = PageRequest.of(page, size);
        Page<HotelRoom> hotelRooms = hotelRoomRepo.findAllByHotel_HotelId(hotelId, pageable);

        // Fetch all room images for each room
        for (HotelRoom room : hotelRooms) {
            List<RoomImage> roomImages = roomImageRepo.findByHotelRoom(room);
            for (RoomImage roomImage : roomImages) {
                System.out.println("Room image first : " + roomImages);
                System.out.println("Room image second iteration: " + roomImage);
                room.setRoomImages(roomImages);
            }
        }
        System.out.println("Hotel rooms final: " + hotelRooms);
        return hotelRooms;
    }

    @Transactional
    public List<RoomImage> getRoomImagesOfRoom(Long roomId) {
        // Fetch the specific room based on roomId
        Optional<HotelRoom> optionalHotelRoom = hotelRoomRepo.findById(roomId);
        if (optionalHotelRoom.isPresent()) {
            HotelRoom room = optionalHotelRoom.get();
            return room.getRoomImages();
        }
        return new ArrayList<>(); // Return an empty list if room or images not found
    }

    @Transactional
    public String updateRoom(
            Long roomId,
            RoomDto roomDto,
            MultipartFile mainRoomImage,
            MultipartFile roomImage1,
            MultipartFile roomImage2,
            MultipartFile roomImage3
    ) {
        try {
            System.out.println("room data: from vendor service " + roomDto);
            System.out.println("main room image: from vendor service " + mainRoomImage.getOriginalFilename());
//            System.out.println("room image 1: " + roomImage1.getOriginalFilename());
//            System.out.println("room image 2: " + roomImage2.getOriginalFilename());
//            System.out.println("room image 3: " + roomImage3.getOriginalFilename());

            // Convert DTO to HotelRoom entity
            HotelRoom updatedRoom = valueMapper.conversionToHotelRoom(roomDto);

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication.isAuthenticated()) {
                String vendorName = authentication.getName();

                // Fetch the room based on room ID and vendor name
                Optional<HotelRoom> optionalRoom = hotelRoomRepo.findById(roomId);
                if (optionalRoom.isPresent()) {
                    HotelRoom room = optionalRoom.get();

                    // Update room details
                    room.setRoomNumber(updatedRoom.getRoomNumber().isEmpty() ? room.getRoomNumber() : updatedRoom.getRoomNumber());
                    room.setRoomType(updatedRoom.getRoomType() == null ? room.getRoomType() : updatedRoom.getRoomType());
                    room.setRoomCategory(updatedRoom.getRoomCategory() == null ? room.getRoomCategory() : updatedRoom.getRoomCategory());
                    room.setRoomBed(updatedRoom.getRoomBed() == null ? room.getRoomBed() : updatedRoom.getRoomBed());
                    room.setRoomPrice(updatedRoom.getRoomPrice() == null ? room.getRoomPrice() : updatedRoom.getRoomPrice());
                    room.setRoomDescription(updatedRoom.getRoomDescription().isEmpty() ? room.getRoomDescription() : updatedRoom.getRoomDescription());
                    room.setHasAC(updatedRoom.getHasAC() == null ? room.getHasAC() : updatedRoom.getHasAC());
                    room.setHasBalcony(updatedRoom.getHasBalcony() == null ? room.getHasBalcony() : updatedRoom.getHasBalcony());
                    room.setHasRefridge(updatedRoom.getHasRefridge() == null ? room.getHasRefridge() : updatedRoom.getHasRefridge());
                    room.setHasTV(updatedRoom.getHasTV() == null ? room.getHasTV() : updatedRoom.getHasTV());
                    room.setHasWifi(updatedRoom.getHasWifi() == null ? room.getHasWifi() : updatedRoom.getHasWifi());
                    room.setRoomStatus(updatedRoom.getRoomStatus().isEmpty() ? room.getRoomStatus() : updatedRoom.getRoomStatus());
                    room.setUpdatedAt(ZonedDateTime.now());

                    // Save the updated room
                    hotelRoomRepo.save(room);

                    //update the room image
                    if (!mainRoomImage.isEmpty() || mainRoomImage != null) {
                        boolean isMainRoomImageUploaded = fileUploaderHelper.fileUploader(mainRoomImage);
                        if (isMainRoomImageUploaded) {
                            room.setMainRoomImage(mainRoomImage.getOriginalFilename());
                            hotelRoomRepo.save(room);
                        }
                    }

                    //update the side room image
                    if (roomImage1 != null) {
                        boolean isRoomImage1Uploaded = fileUploaderHelper.fileUploader(roomImage1);
                        if (isRoomImage1Uploaded) {
                            RoomImage roomImageObj = new RoomImage();
                            roomImageObj.setImageUrl(roomImage1.getOriginalFilename());
                            roomImageObj.setHotelRoom(room);
                            roomImageObj.setCreatedAt(ZonedDateTime.now());
                            roomImageObj.setImageStatus("ACTIVE");
                            roomImageRepo.save(roomImageObj);
                        }
                    }

                    if (roomImage2 != null) {
                        boolean isRoomImage2Uploaded = fileUploaderHelper.fileUploader(roomImage2);
                        if (isRoomImage2Uploaded) {
                            RoomImage roomImageObj = new RoomImage();
                            roomImageObj.setImageUrl(roomImage2.getOriginalFilename());
                            roomImageObj.setHotelRoom(room);
                            roomImageObj.setCreatedAt(ZonedDateTime.now());
                            roomImageObj.setImageStatus("ACTIVE");
                            roomImageRepo.save(roomImageObj);
                        }
                    }
                    if (roomImage3 != null) {
                        boolean isRoomImage3Uploaded = fileUploaderHelper.fileUploader(roomImage3);
                        if (isRoomImage3Uploaded) {
                            RoomImage roomImageObj = new RoomImage();
                            roomImageObj.setImageUrl(roomImage3.getOriginalFilename());
                            roomImageObj.setHotelRoom(room);
                            roomImageObj.setCreatedAt(ZonedDateTime.now());
                            roomImageObj.setImageStatus("ACTIVE");
                            roomImageRepo.save(roomImageObj);
                        }
                    }
                    return "Room updated successfully";
                } else {
                    return "Room not found for the given ID and vendor";
                }
            } else {
                return "Authentication failed";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to update room";
        }
    }

    @Transactional
    public String deleteSpecificRoom(long roomId) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication.isAuthenticated()) {
                String username = authentication.getName();
                User vendor = userRepo.findByUserName(username);
                Hotel hotel = hotelRepo.findByUser(vendor);

                HotelRoom hotelRoom = hotelRoomRepo.findByRoomId(roomId);
                hotelRoom.setIsDeleted(true);
                hotelRoomRepo.save(hotelRoom);

                return "Room deleted successfully";
            }
            return "Authentication failed";
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to delete room";
        }
    }

    //post report and issue by the vendor
    @Transactional
    public String postReport(Report report) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication.isAuthenticated()) {
                String username = authentication.getName();

                User vendorObject = userRepo.findByUserName(username);

                report.setUser(vendorObject);

                reportRepository.save(report);

                return "Report posted successfully";
            }
            return "Authentication failed";
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to post report";
        }
    }

    //get hotel details only
    @Transactional
    public Hotel getHotelDetailsService() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.isAuthenticated()) {
            String username = authentication.getName();
            User vendor = userRepo.findByUserName(username);
            return hotelRepo.findByUser(vendor);
        }
        return null;
    }

    //get all booked or refunded or cancelled or pending bookings
    @Transactional
    public List<VendorBookingDTO> getBookings(String status, int page, int size) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String vendorName = authentication.getName();

        User vendor = userRepo.findByUserName(vendorName);
        Hotel hotel = hotelRepo.findByUser(vendor);

        List<Booking> bookings = hotelDAO.getBookingDetails(status, hotel.getHotelId(), page, size);

        List<VendorBookingDTO> vendorBookingDTOS = new ArrayList<>();

        for (Booking booking : bookings) {
            VendorBookingDTO vendorBookingDTO = new VendorBookingDTO();

            vendorBookingDTO.setBookingId(booking.getBookingId());
            vendorBookingDTO.setCheckInDate(booking.getCheckInDate().toString());
            vendorBookingDTO.setCheckOutDate(booking.getCheckOutDate().toString());
            vendorBookingDTO.setBookingDate(booking.getBookingDate().toString());
            vendorBookingDTO.setStatus(booking.getStatus().toString());
            vendorBookingDTO.setPaymentMethod(booking.getPaymentMethod().getPaymentMethodName());
            vendorBookingDTO.setTotalAmount(booking.getTotalAmount());


            HotelRoom hotelRoom = hotelRoomRepo.findByRoomId(booking.getHotelRoom().getRoomId());
            vendorBookingDTO.setRoomId(hotelRoom.getRoomId());
            vendorBookingDTO.setBedCategory(hotelRoom.getRoomBed().toString());
            vendorBookingDTO.setRoomPrice(hotelRoom.getRoomPrice().toString());
            vendorBookingDTO.setRoomType(hotelRoom.getRoomType().toString());
            vendorBookingDTO.setRoomDescription(hotelRoom.getRoomDescription());
            vendorBookingDTO.setHotelRoomImage(hotelRoom.getMainRoomImage());

            vendorBookingDTO.setBedType(hotelRoom.getRoomBed().toString());

            vendorBookingDTO.setHasAC(hotelRoom.getHasAC());
            vendorBookingDTO.setHasBalcony(hotelRoom.getHasBalcony());
            vendorBookingDTO.setHasRefridge(hotelRoom.getHasRefridge());
            vendorBookingDTO.setHasTV(hotelRoom.getHasTV());


            vendorBookingDTO.setUser(booking.getUser());
            vendorBookingDTO.setVendorUpdated(booking.getVendorUpdated());

            vendorBookingDTOS.add(vendorBookingDTO);
        }

        return vendorBookingDTOS;
    }

    @Transactional
    public String updateBooking(long bookingId, long userId, String status) {
        try {
            // Update the booking status
            Boolean updated = this.hotelDAO.updateBookingStatus(bookingId, status);

            // Retrieve user information
            User user = this.userRepo.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));

            // If booking status was successfully updated
            if (updated) {

                //set the value of room status to unavailable
                if (status.equals("BOOKED")) {
                    Booking booking = bookingRepository.findById(bookingId).get();
                    HotelRoom hotelRoom = booking.getHotelRoom();
                    hotelRoom.setRoomStatus("BOOKED");
                    hotelRoomRepo.save(hotelRoom);
                }

                String emailSubject = "Booking status updated";
                String emailBody = "Your booking status has been updated to: " + status;

                emailSenderService.sendEmail(user.getUserEmail(), emailSubject, emailBody);

                // Return success message
                return "Successfully Changed";
            } else {
                // Return null if booking status was not updated
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace(); // Log the exception for debugging
            return null; // Return null in case of any error
        }
    }

    @Transactional
    public VendorDashboardAnalyticsDTO getVendorAnalyticsService() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User vendor = userRepo.findByUserName(username);
        Hotel hotel = hotelRepo.findByUser(vendor);

        VendorDashboardAnalyticsDTO vendorDashboardAnalyticsDTO = new VendorDashboardAnalyticsDTO();

        //get the total number of bookings
        long totalBookings = hotelDAO.getTotalBookings(hotel.getHotelId());
        vendorDashboardAnalyticsDTO.setTotalBookings(totalBookings);

//        //get the total number of bookings
        long totalRefunds = hotelDAO.getTotalRefundedBookings(hotel.getHotelId());
        vendorDashboardAnalyticsDTO.setTotalRefundedRooms(totalRefunds);
//
//        //get the total number of bookings
        long totalCancelled = hotelDAO.getTotalCancelledsBookings(hotel.getHotelId());
        vendorDashboardAnalyticsDTO.setTotalCancelledRooms(totalCancelled);
//
//        //get the total number of bookings
        long totalPending = hotelDAO.getTotalPendingBookings(hotel.getHotelId());
        vendorDashboardAnalyticsDTO.setTotalPendingRooms(totalPending);

//        get the total booked number of bookings
        long totalBooking = hotelDAO.getTotalBookedBookings(hotel.getHotelId());
        vendorDashboardAnalyticsDTO.setTotalBookedRooms(totalBooking);

//        get total revenue of the hotel based on the hotel id
        long totalRevenue = hotelDAO.getHotelTotalRevenue(hotel.getHotelId());
        vendorDashboardAnalyticsDTO.setTotalRevenue(totalRevenue);

//       get total number of arriving hotel rooms today
        long totalArrivingRoomsToday = hotelDAO.getTodayTotalArrivingRooms(hotel.getHotelId(), LocalDate.now());
        vendorDashboardAnalyticsDTO.setArrivalsToday(totalArrivingRoomsToday);

        // get total number of tomorrow check in date booking
        long totalArrivingRoomsTomorrow = hotelDAO.getTomorrowTotalArrivingRooms(hotel.getHotelId());
        vendorDashboardAnalyticsDTO.setArrivalsTomorrow(totalArrivingRoomsTomorrow);

        long totalAvailableRoom = this.hotelDAO.getTodayAvailableRoom(hotel.getHotelId());
        vendorDashboardAnalyticsDTO.setTotalAvailableRooms(totalAvailableRoom);

        return vendorDashboardAnalyticsDTO;
    }


    //get all the review of a specific hotel based on the user id
    @Transactional
    public List<ReviewDTO> getHotelReviews() {

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication.isAuthenticated()) {
                String username = authentication.getName();
                User vendor = userRepo.findByUserName(username);
                Hotel hotel = hotelRepo.findByUser(vendor);

                return hotelDAO.getHotelReviews(hotel.getHotelId());
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Transactional
//    @Cacheable(value = "vendorRevenue", key = "#root.methodName")
    public VendorRevenueDTO getVendorRevenue() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User vendor = userRepo.findByUserName(username);
        Hotel hotel = hotelRepo.findByUser(vendor);

        VendorRevenueDTO vendorRevenueDTO = new VendorRevenueDTO();

        PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod = this.paymentMethodRepository.findByPaymentMethodName("Cash");

        //extract all the cash on arrival revenue from booking table based on the hotel id
        long cashOnArrival = this.bookingDAO.getTotalCashOnArrivalRevenue(hotel.getHotelId(), paymentMethod);
        vendorRevenueDTO.setCashOnArrival(cashOnArrival);

        //extract all the khalti revenue from booking table based on the hotel id
        PaymentMethod paymentMethod1 = new PaymentMethod();
        paymentMethod1 = this.paymentMethodRepository.findByPaymentMethodName("KHALTI");

        long khaltiRevenue = this.bookingDAO.getTotalKhaltiRevenue(hotel.getHotelId(), paymentMethod1);

        vendorRevenueDTO.setKhaltiPayment(khaltiRevenue);

        return vendorRevenueDTO;
    }

    @Transactional
    public List<HotelRoom> getRoomDetails(Long roomId) {
        HotelRoom hotelRooms = hotelRoomRepo.findByRoomId(roomId);
        List<HotelRoom> room = new ArrayList<>();
        room.add(hotelRooms);
        return room;
    }


    @Transactional
    public List<BookingStatusDTO> getBookingStatusDetails(long roomId) {
        return bookingDAO.getBookingStatusDetailsByRoomId(roomId);
    }

    //get all the booking details of a specific room
    @Transactional
    public List<RoomHistoryDTO> getRoomHistory(long roomId) {
        List<Booking> bookings = bookingRepository.findByHotelRoom_RoomId(roomId);
        List<RoomHistoryDTO> roomHistoryDTOs = new ArrayList<>();
        for (Booking booking : bookings) {
            RoomHistoryDTO roomHistoryDTO = new RoomHistoryDTO();

            roomHistoryDTO.setCheckInDate(booking.getCheckInDate());
            roomHistoryDTO.setCheckOutDate(booking.getCheckOutDate());
            roomHistoryDTO.setBookingDate(booking.getBookingDate());
            roomHistoryDTO.setStatus(booking.getStatus());
            roomHistoryDTO.setPaymentMethod(booking.getPaymentMethod().getPaymentMethodName());
            roomHistoryDTO.setTotalAmount(booking.getTotalAmount());
            roomHistoryDTO.setUserName(booking.getUser().getUsername());

            roomHistoryDTOs.add(roomHistoryDTO);
        }
        return roomHistoryDTOs;
    }

    //check if the room is already registered or not
    @Transactional
    public Boolean checkRoomExistsOrNot(String roomNumber) {
        String vendorName = SecurityContextHolder.getContext().getAuthentication().getName();
        Hotel hotel = hotelRepo.findByUser(userRepo.findByUserName(vendorName));
        return hotelDAO.RoomExistOrNot(roomNumber, hotel.getHotelId());
    }

    @Transactional
    public Hotel getHotelDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.isAuthenticated()) {
            String username = authentication.getName();
            User vendor = userRepo.findByUserName(username);
            return hotelRepo.findByUser(vendor);
        }
        return null;
    }

    @Transactional
    public String updateHotelDetails(
            String hotelName,
            String hotelAddress,
            String hotelContact,
            String hotelEmail,
            String hotelPan,
            String hotelDescription,
            int hotelStar,
            MultipartFile hotelImage
    ) {
        System.out.println("hotel update service");

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userName = authentication.getName();

            User vendor = userRepo.findByUserName(userName);

            Hotel hotel = hotelRepo.findByUser(vendor);

            if (hotelImage != null) {
                boolean isHotelImageUploaded = fileUploaderHelper.fileUploader(hotelImage);
                if (isHotelImageUploaded) {
                    hotel.setHotelImage(hotelImage.getOriginalFilename());
                }
            }

            if (hotelName != null) {
                hotel.setHotelName(hotelName);
            }
            if (hotelAddress != null) {
                hotel.setHotelAddress(hotelAddress);
            }
            if (hotelContact != null) {
                hotel.setHotelContact(hotelContact);
            }
            if (hotelEmail != null) {
                hotel.setHotelEmail(hotelEmail);
            }
            if (hotelPan != null) {
                hotel.setHotelPan(hotelPan);
            }
            if (hotelDescription != null) {
                hotel.setHotelDescription(hotelDescription);
            }
            if (hotelStar != 0) {
                hotel.setHotelStar(hotelStar);
            }

            hotel.setUpdatedAt(Instant.now());
            hotelRepo.save(hotel);

            return "Hotel details updated successfully";
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to update hotel details";
        }
    }

}
