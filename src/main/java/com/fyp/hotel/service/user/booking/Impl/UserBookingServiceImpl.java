package com.fyp.hotel.service.user.booking.Impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fyp.hotel.dao.booking.BookingDAO;
import com.fyp.hotel.dao.booking.PaymentMethodDAO;
import com.fyp.hotel.dao.hotel.HotelDAO;
import com.fyp.hotel.dto.booking.BookDto;
import com.fyp.hotel.dto.booking.BookingDTO;
import com.fyp.hotel.dto.booking.BookingStatusDTO;
import com.fyp.hotel.dto.khalti.CustomerInfo;
import com.fyp.hotel.dto.khalti.KhaltiInitationRequest;
import com.fyp.hotel.dto.khalti.KhaltiResponseDTO;
import com.fyp.hotel.dto.room.CheckRoomAvailabilityDto;
import com.fyp.hotel.enums.Status;
import com.fyp.hotel.model.*;
import com.fyp.hotel.repository.*;
import com.fyp.hotel.service.user.booking.UserBookingService;
import com.fyp.hotel.util.DaysChecker;
import com.fyp.hotel.util.KhaltiPayment;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.client.HttpClientErrorException;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Service
@Transactional
@Slf4j
public class UserBookingServiceImpl implements UserBookingService {

    private final UserRepository userRepo;
    private final HotelRoomRepository hotelRoomRepository;
    private final PaymentMethodDAO paymentMethodDAO;
    private final DaysChecker daysChecker;
    private final BookingRepository bookingRepository;
    private final PaymentRepository paymentRepository;
    private final BookingDAO bookingDAO;
    private final HotelDAO hotelDAO;
    private final KhaltiPayment khaltiPayment;

    @Autowired
    public UserBookingServiceImpl(
            UserRepository userRepo,
            HotelRoomRepository hotelRoomRepository,
            PaymentMethodDAO paymentMethodDAO,
            DaysChecker daysChecker,
            BookingRepository bookingRepository,
            PaymentRepository paymentRepository,
            BookingDAO bookingDAO,
            HotelDAO hotelDAO,
            KhaltiPayment khaltiPayment) {
        this.userRepo = userRepo;
        this.hotelRoomRepository = hotelRoomRepository;
        this.paymentMethodDAO = paymentMethodDAO;
        this.daysChecker = daysChecker;
        this.bookingRepository = bookingRepository;
        this.paymentRepository = paymentRepository;
        this.bookingDAO = bookingDAO;
        this.hotelDAO = hotelDAO;
        this.khaltiPayment = khaltiPayment;
    }

    @Override
    public String cashOnArrivalTransaction(@Validated BookDto bookDto) {
        if(bookDto.getPaymentMethod().equals("Cash")){
            Authentication authenticatedUser = SecurityContextHolder.getContext().getAuthentication();
            String userName = authenticatedUser.getName();

            User user = userRepo.findByUserName(userName);
            HotelRoom hotelRoom = hotelRoomRepository.findByRoomId(bookDto.getRoomId());
            PaymentMethod paymentMethodObject = paymentMethodDAO.getPaymentMethod(bookDto.getPaymentMethod());

            long daysOfStay = daysChecker.getDays(bookDto.getCheckInDate(), bookDto.getCheckOutDate());
            long totalAmount = Long.valueOf(hotelRoom.getRoomPrice().intValue() * daysOfStay);

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
            booking.setVendorUpdated(false);

            bookingRepository.save(booking);

            return "Payment successful by cash on arrival";
        }
        return "Payment failed";
    }

    @Override
    public KhaltiResponseDTO onlinePaymentTransaction(
            @Validated BookDto bookDto
    ) {
        if (bookDto.getPaymentMethod().equals("khalti")) {
            try {
                HotelRoom hotelRoom = hotelRoomRepository.findByRoomId(bookDto.getRoomId());
                Long hotelId = hotelRoom.getHotel().getHotelId();
                PaymentMethod paymentMethodObject = paymentMethodDAO.getPaymentMethod(bookDto.getPaymentMethod().toUpperCase());

                Authentication authenticatedUser = SecurityContextHolder.getContext().getAuthentication();
                String userName = authenticatedUser.getName();

                Long roomId = hotelRoom.getRoomId();
                Double roomPrice = hotelRoom.getRoomPrice();

                long daysOfStay = daysChecker.getDays(bookDto.getCheckInDate(), bookDto.getCheckOutDate());
                long totalAmount = Long.valueOf(roomPrice.intValue() * daysOfStay);

                User user = userRepo.findByUserName(userName);

                String return_url = "http://localhost:3000/hotel/" + hotelId + "/room/" + bookDto.getRoomId() + "/" + hotelRoom.getRoomPrice();
                String website_url = "http://localhost:3000/";

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
                booking.setVendorUpdated(true);

                Booking bookingObj = bookingRepository.save(booking);

                KhaltiInitationRequest khaltiInitiationRequest = new KhaltiInitationRequest();

                khaltiInitiationRequest.setReturn_url(return_url);
                khaltiInitiationRequest.setWebsite_url(website_url);
                long khaltiTotalAmount = totalAmount * 100;
                khaltiInitiationRequest.setAmount(khaltiTotalAmount);
                khaltiInitiationRequest.setPurchase_order_id(bookingObj.getBookingId().toString());
                khaltiInitiationRequest.setPurchase_order_name(hotelRoom.getRoomNumber().toString());

                CustomerInfo customerInfo = new CustomerInfo(user.getUsername(), user.getUserFirstName(), user.getUserLastName(), user.getUserEmail(), user.getUserPhone(), user.getUserAddress());
                khaltiInitiationRequest.setCustomerInfo(customerInfo);

                KhaltiResponseDTO res =  this.khaltiPayment.callKhalti(khaltiInitiationRequest);
                return res;

            } catch (JsonProcessingException e) {
                log.error("Error processing JSON", e);
                throw new RuntimeException("Error processing JSON", e);
            } catch (HttpClientErrorException e) {
                log.error("Error communicating with Khalti API", e);
                throw new RuntimeException("Error communicating with Khalti API", e);
            } catch (Exception e) {
                log.error("Unexpected error", e);
                throw new RuntimeException("Unexpected error", e);
            }
        }
        return null;
    }

    @Override
    public String updatePaymentTransaction(String pidx, String status, String bookingId, long totalAmount) {
        try {
            HotelRoom hotelRoom = this.hotelRoomRepository.findByBooking_BookingId(Long.parseLong(bookingId));
            if (hotelRoom != null) {
                hotelRoom.setRoomStatus("BOOKED");
                this.hotelRoomRepository.save(hotelRoom);
            } else {
                throw new RuntimeException("Hotel room not found for booking ID: " + bookingId);
            }

            Booking booking = this.bookingRepository.findByBookingId(Long.parseLong(bookingId));
            if (booking != null) {
                booking.setStatus(Status.BOOKED);
                this.bookingRepository.save(booking);
            } else {
                throw new RuntimeException("Booking not found with ID: " + bookingId);
            }

            Payment payment = new Payment();
            payment.setPaymentAmount(totalAmount);
            payment.setCreatedAt(Instant.now());
            payment.setBooking(booking);
            payment.setPaymentStatus(status);
            payment.setTransactionId(pidx);
            payment.setPaymentDate(LocalDate.now());

            this.paymentRepository.save(payment);

            return "SuccessFull enter";
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to update payment table";
        }
    }

    @Override
    public CheckRoomAvailabilityDto checkRoomAvailability(
            Long roomId
    ){
        List<Booking> bookings = hotelDAO.getBookingsForToday(roomId);
        if(bookings.size() > 0){
            CheckRoomAvailabilityDto checkRoomAvailabilityDto = new CheckRoomAvailabilityDto();
            checkRoomAvailabilityDto.setRoomId(roomId);
            checkRoomAvailabilityDto.setStatus(bookings.get(0).getStatus().toString());
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

    @Override
    public List<BookingDTO> viewBookingDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userRepo.findByUserName(userName);

        if (user == null) {
            return Collections.emptyList();
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

    @Override
    public List<BookingStatusDTO> getBookingStatus(Long hotelId) {
        return bookingDAO.getBookingStatusDetails(hotelId);
    }
}