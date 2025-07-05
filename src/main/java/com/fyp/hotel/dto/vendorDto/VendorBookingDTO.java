package com.fyp.hotel.dto.vendorDto;

import com.fyp.hotel.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VendorBookingDTO {

    private long bookingId;
    private String checkInDate;
    private String checkOutDate;
    private String bookingDate;
    private String status;
    private Long totalAmount;
    private String paymentMethod;
    private User user;
    private long roomId;
    private String bedCategory;
    private String bedType;
    private String roomPrice;
    private String roomType;
    private String roomDescription;
    private String hotelRoomImage;
    private Boolean hasAC;
    private Boolean hasBalcony;
    private Boolean hasTV;
    private Boolean hasRefridge;
    private Boolean vendorUpdated;
}