package com.fyp.hotel.model;

import java.time.ZonedDateTime;
import java.util.List;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "hotel_room")
public class HotelRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //identity means auto increment
    @Column(name = "room_id")
    private Long roomId;

    @Column(name = "room_number", nullable = false, unique = false)
    @Basic // to avoid lazy initialization exception
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

    @Column(name = "created_at", nullable = false, unique = false)
    private ZonedDateTime createdAt;

    @Column(name = "updated_at", nullable = true, unique = false)
    private ZonedDateTime updatedAt;

    @Column(name = "main_room_image", nullable = false, unique = false)
    private String mainRoomImage;

    @ManyToOne(fetch = FetchType.LAZY) //no cascading as deletion of room doesnot delete hotel
    @JoinColumn(name = "hotel_id")
    private Hotel hotel; // Many HotelRoom objects can belong to one Hotel

    @OneToMany(mappedBy = "hotelRoom", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<RoomImage> roomImages; // One HotelRoom can have many RoomImage objects
}
