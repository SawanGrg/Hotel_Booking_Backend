package com.fyp.hotel.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class CheckRoomAvailabilityDto {
    private String checkInDate;
    private String checkOutDate;
    private Long roomId;
    private String Status;

    public CheckRoomAvailabilityDto(String checkInDate, String checkOutDate, Long roomId, String Status) {
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.roomId = roomId;
        this.Status = Status;
    }

    public CheckRoomAvailabilityDto() {

    }
}
