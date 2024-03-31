package com.fyp.hotel.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DisplayHotelWithAmenitiesDto {

    private long hotelId;
    private String hotelName;
    private String hotelContact;
    private String hotelAddress;
    private String hotelEmail;
    private String hotelDescription;
    private String hotelImage;
    private String hotelPan;
    private String hotelStatus;

    private long hotelRating;

    private Boolean hasWifi;
    private Boolean hasFridge;
    private Boolean hasAC;
    private Boolean hasTV;
    private Boolean hasBalcony;

    public DisplayHotelWithAmenitiesDto() {
    }

    public DisplayHotelWithAmenitiesDto(long hotelId,String hotelName, String hotelContact, String hotelAddress, String hotelEmail, String hotelDescription, String hotelImage, String hotelPan, String hotelStatus, Boolean hasWifi, Boolean hasFridge, Boolean hasAC, Boolean hasTV, Boolean hasBalcony) {
        this.hotelId = hotelId;
        this.hotelName = hotelName;
        this.hotelContact = hotelContact;
        this.hotelAddress = hotelAddress;
        this.hotelEmail = hotelEmail;
        this.hotelDescription = hotelDescription;
        this.hotelImage = hotelImage;
        this.hotelPan = hotelPan;
        this.hotelStatus = hotelStatus;
        this.hasWifi = hasWifi;
        this.hasFridge = hasFridge;
        this.hasAC = hasAC;
        this.hasTV = hasTV;
        this.hasBalcony = hasBalcony;
    }

    @Override
    public String toString() {
        return "DisplayHotelWithAmenitiesDto{" +
                "hotelId=" + hotelId +
                ", hotelName='" + hotelName + '\'' +
                ", hotelContact='" + hotelContact + '\'' +
                ", hotelAddress='" + hotelAddress + '\'' +
                ", hotelEmail='" + hotelEmail + '\'' +
                ", hotelDescription='" + hotelDescription + '\'' +
                ", hotelImage='" + hotelImage + '\'' +
                ", hotelPan='" + hotelPan + '\'' +
                ", hotelStatus='" + hotelStatus + '\'' +
                ", hotelRating=" + hotelRating +
                ", hasWifi=" + hasWifi +
                ", hasFridge=" + hasFridge +
                ", hasAC=" + hasAC +
                ", hasTV=" + hasTV +
                ", hasBalcony=" + hasBalcony +
                '}';
    }

}
