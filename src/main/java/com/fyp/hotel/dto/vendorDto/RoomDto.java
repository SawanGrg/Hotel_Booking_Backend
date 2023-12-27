package com.fyp.hotel.dto.vendorDto;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public class RoomDto {

    @NotNull(message = "Room number cannot be null")
    @Length(min = 4, max = 50, message = "Room number must be between 3 and 50 characters")
    private String roomNumber;
    @NotNull(message = "Room type cannot be null")
    @Length(min = 4, max = 50, message = "Room type must be between 3 and 50 characters")
    private String roomType;
    @NotNull(message = "Room category cannot be null")
    private String roomCategory;
    private String roomBed;
    private Double roomPrice;
    private String roomDescription;
    private String roomStatus;
    private Boolean hasBalcony;
    private Boolean hasAC;
    private Boolean hasRefridge;
    private Boolean hasTV;
    private Boolean hasWifi;
    @NotNull(message = "Room image cannot be null")
    private String mainRoomImage;

    public RoomDto() {
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public String getRoomCategory() {
        return roomCategory;
    }

    public void setRoomCategory(String roomCategory) {
        this.roomCategory = roomCategory;
    }

    public String getRoomBed() {
        return roomBed;
    }

    public void setRoomBed(String roomBed) {
        this.roomBed = roomBed;
    }

    public Double getRoomPrice() {
        return roomPrice;
    }

    public void setRoomPrice(Double roomPrice) {
        this.roomPrice = roomPrice;
    }

    public String getRoomDescription() {
        return roomDescription;
    }

    public void setRoomDescription(String roomDescription) {
        this.roomDescription = roomDescription;
    }

    public String getRoomStatus() {
        return roomStatus;
    }

    public void setRoomStatus(String roomStatus) {
        this.roomStatus = roomStatus;
    }

    public Boolean getHasBalcony() {
        return hasBalcony;
    }

    public void setHasBalcony(Boolean hasBalcony) {
        this.hasBalcony = hasBalcony;
    }

    public Boolean getHasAC() {
        return hasAC;
    }

    public void setHasAC(Boolean hasAC) {
        this.hasAC = hasAC;
    }

    public Boolean getHasRefridge() {
        return hasRefridge;
    }

    public void setHasRefridge(Boolean hasRefridge) {
        this.hasRefridge = hasRefridge;
    }

    public Boolean getHasTV() {
        return hasTV;
    }

    public void setHasTV(Boolean hasTV) {
        this.hasTV = hasTV;
    }

    public Boolean getHasWifi() {
        return hasWifi;
    }

    public void setHasWifi(Boolean hasWifi) {
        this.hasWifi = hasWifi;
    }

    public String getMainRoomImage() {
        return mainRoomImage;
    }
    public void setMainRoomImage(String mainRoomImage) {
        this.mainRoomImage = mainRoomImage;
    }

}
