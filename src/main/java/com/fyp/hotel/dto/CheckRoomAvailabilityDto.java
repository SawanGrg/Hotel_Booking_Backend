package com.fyp.hotel.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CheckRoomAvailabilityDto {
    private String checkInDate;
    private String checkOutDate;
    private Long roomId;
    private String Status;
    private String userName;
    private String roomNumber;

    public CheckRoomAvailabilityDto(String checkInDate, String checkOutDate, Long roomId, String Status, String userName, String roomNumber) {
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.roomId = roomId;
        this.Status = Status;
        this.userName = userName;
        this.roomNumber = roomNumber;
    }

    public CheckRoomAvailabilityDto() {

    }
}
