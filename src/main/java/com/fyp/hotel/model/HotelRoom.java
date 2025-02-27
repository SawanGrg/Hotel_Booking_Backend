package com.fyp.hotel.model;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fyp.hotel.enums.HotelBedType;
import com.fyp.hotel.enums.HotelRoomCategory;
import com.fyp.hotel.enums.HotelRoomType;
import jakarta.persistence.*;
import lombok.Data;

//JsonIdentityInfo is used to avoid infinite recursion between hotel and hotel room
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class, //property generator is used to generate id for hotel room
        property = "roomId" //property is used to identify the property of hotel room
)
@Data
@Entity
@Table(name = "hotel_room")
public class HotelRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //identity means auto increment
    @Column(name = "room_id")
    private Long roomId;

    @Column(name = "room_number", nullable = false, unique = false)
    @Basic //basic is used to avoid lazy initialization exception
    private String roomNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "room_type", nullable = false, unique = false)
    private HotelRoomType roomType;

    @Enumerated(EnumType.STRING)
    @Column(name = "bed_category", nullable = false, unique = false)
    private HotelRoomCategory roomCategory;

    @Enumerated(EnumType.STRING)
    @Column(name = "bed_type", nullable = false, unique = false)
    private HotelBedType roomBed;

    @Column(name = "room_price", nullable = false, unique = false)
    private Double roomPrice;

    @Column(name = "room_description", nullable = false, unique = false)
    private String roomDescription;

    @Column(name = "room_status", nullable = false, unique = false)
    private String roomStatus;

    //Columns like hasBalcony, hasAC, hasRefridge etc.

    @Column(name = "has_balcony", nullable = false, unique = false)
    private Boolean hasBalcony;

    @Column(name = "has_ac", nullable = false, unique = false)
    private Boolean hasAC;

    @Column(name = "has_refridge", nullable = false, unique = false)
    private Boolean hasRefridge;

    @Column(name = "has_tv", nullable = false, unique = false)
    private Boolean hasTV;

    @Column(name = "has_wifi", nullable = false, unique = false)
    private Boolean hasWifi;

    //is room deleted
    @Column(name = "is_deleted", nullable = false, columnDefinition = "boolean default false")
    private Boolean isDeleted;


    @Column(name = "created_at", nullable = false, unique = false)
    private ZonedDateTime createdAt;

    @Column(name = "updated_at", nullable = true, unique = false)
    private ZonedDateTime updatedAt;

    @Column(name = "main_room_image", nullable = false, unique = false)
    private String mainRoomImage;

    @JsonIgnore //to avoid infinite recursion between hotel and hotel room
    @ManyToOne(fetch = FetchType.EAGER) //no cascading as deletion of room doesnot delete hotel
    @JoinColumn(name = "hotel_id")
    private Hotel hotel; // Many HotelRoom objects can belong to one Hotel


    // One HotelRoom can have many RoomImage objects
    //mappedBy = "hotelRoom" means that the HotelRoom object is the owner of the relationship
    //cascade = CascadeType.ALL means that if a HotelRoom object is deleted, all its RoomImage objects will also be deleted
    //orphanRemoval = true means that if a RoomImage object is removed from the HotelRoom object, it will be deleted from the database
    //@JsonBackReference
    @OneToMany(mappedBy = "hotelRoom", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<RoomImage> roomImages = new ArrayList<>();// One HotelRoom can have many RoomImage objects

    @JsonBackReference //json back reference is used to avoid infinite recursion between hotel room and booking
    //one room can be booked one time at a time
    @OneToMany(mappedBy = "hotelRoom", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Booking> booking;

    @Override
    public String toString() {
        return "Room Id : " + this.roomId + " Room Number : "
                + this.roomNumber + " Room Type : " + this.roomType + " Room Category : " + this.roomCategory + " Room Bed : "
                + this.roomBed + " Room Price : " + this.roomPrice + " Room Description : " + this.roomDescription + " Room Status : "
                + this.roomStatus + " Has Balcony : " + this.hasBalcony + " Has AC : " + this.hasAC + " Has Refridge : " + this.hasRefridge + " Has TV : "
                + this.hasTV + " Has Wifi : " + this.hasWifi + " Created At : " + this.createdAt + " Updated At : " + this.updatedAt + " Main Room Image : "
                + this.mainRoomImage + " Hotel : " + this.hotel + " Room Images : " + this.roomImages.size();
    }

}
