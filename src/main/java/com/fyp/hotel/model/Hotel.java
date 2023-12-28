package com.fyp.hotel.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@Entity
@Table(name = "hotel")
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hotel_id")
    private Long hotelId;

    @Column(name = "hotel_name", nullable = false, unique = true, length = 50)
    private String hotelName;

    @Column(name = "hotel_address", nullable = false, unique = false, length = 50)
    private String hotelAddress;

    @Column(name = "hotel_contact", nullable = false, unique = true, length = 50)
    private String hotelContact;

    @Column(name = "hotel_email", nullable = false, unique = true, length = 50)
    private String hotelEmail;

    @Column(name = "hotel_pan", nullable = false, unique = true, length = 10)
    private String hotelPan;

    @Column(name = "created_at", nullable = false, unique = false, length = 50)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = true, unique = false, length = 50)
    private Instant updatedAt;

    @Column(name = "hotel_status", nullable = false, unique = false, length = 50)
    private String hotelStatus;

    @Column(name = "hotel_image", nullable = false, unique = false, length = 50)
    private String hotelImage;

    //one user has one hotel relationship
    @JsonManagedReference
    @OneToOne(fetch = FetchType.LAZY) //fetch type lazy means that the user will not be fetched until it is called
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @JsonBackReference
    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, fetch = FetchType.EAGER)//deletion of hotel means deletion of the rooms
    private List<HotelRoom> hotelRooms = new ArrayList<>(); // One Hotel can have many HotelRoom objects

    @Override
    public String toString() {
        return "Hotel [hotelId=" + this.hotelId + ", hotelName=" + this.hotelName + ", hotelAddress="
                + this.hotelAddress + ", hotelContact=" + this.hotelContact + ", hotelEmail=" + this.hotelEmail
                 + "]"; // Modify the hotelRooms part
    }
}