package com.fyp.hotel.dto.vendorDto;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotNull;

public class HotelDto {

    private Long hotelId;

    @NotNull(message = "Hotel name cannot be null")
    @Length(min = 4, max = 50, message = "Hotel name must be between 3 and 50 characters")
    private String hotelName;

    @NotNull(message = "Hotel address cannot be null")
    private String hotelAddress;

    @NotNull(message = "Hotel contact cannot be null")
    private String hotelContact;

    @NotNull(message = "Hotel email cannot be null")
    private String hotelEmail;

    @NotNull(message = "Hotel PAN cannot be null")
    private String hotelPan;

    private String createdAt;

    private String updatedAt;

    public HotelDto() {
    }

    public HotelDto(String hotelName, String hotelAddress, String hotelContact, String hotelEmail, String hotelPan,
                    String createdAt, String updatedAt) {
        this.hotelName = hotelName;
        this.hotelAddress = hotelAddress;
        this.hotelContact = hotelContact;
        this.hotelEmail = hotelEmail;
        this.hotelPan = hotelPan;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getHotelId() {
        return hotelId;
    }

    public void setHotelId(Long hotelId) {
        this.hotelId = hotelId;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public String getHotelAddress() {
        return hotelAddress;
    }

    public void setHotelAddress(String hotelAddress) {
        this.hotelAddress = hotelAddress;
    }

    public String getHotelContact() {
        return hotelContact;
    }

    public void setHotelContact(String hotelContact) {
        this.hotelContact = hotelContact;
    }

    public String getHotelEmail() {
        return hotelEmail;
    }

    public void setHotelEmail(String hotelEmail) {
        this.hotelEmail = hotelEmail;
    }

    public String getHotelPan() {
        return hotelPan;
    }

    public void setHotelPan(String hotelPan) {
        this.hotelPan = hotelPan;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "HotelDto [hotelId=" + hotelId + ", hotelName=" + hotelName + ", hotelAddress=" + hotelAddress +
                ", hotelContact=" + hotelContact + ", hotelEmail=" + hotelEmail + ", hotelPan=" + hotelPan +
                ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + "]";
    }
}
