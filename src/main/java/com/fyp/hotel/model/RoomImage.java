package com.fyp.hotel.model;

import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class, //property generator is used to generate id for hotel room
        property = "imageId" //property is used to identify the property of hotel room
)
@Data // generate getters and setters for all fields
@Entity
@Table(name = "room_image")
public class RoomImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long imageId;

    @Column(name = "image_url", nullable = true, unique = false)
    private String imageUrl;

    @Column(name = "image_status", nullable = true, unique = false)
    private String imageStatus;

    @Column(name = "created_at", nullable = true, unique = false)
    private ZonedDateTime createdAt;

    @Column(name = "updated_at", nullable = true, unique = false)
    private ZonedDateTime updatedAt;

    @JsonIgnore //to avoid infinite recursion between hotel and hotel room
    @ManyToOne(fetch = FetchType.LAZY) //many images can be associated with one hotel room
    @JoinColumn(name = "room_id", nullable = false, unique = false)
    private HotelRoom hotelRoom = new HotelRoom();

    @Override
    public String toString() {
        return "RoomImage{" +
                "imageId=" + imageId +
                ", imageUrl='" + imageUrl + '\'' +
                ", imageStatus='" + imageStatus + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", hotelRoom=" + hotelRoom +
                '}';
    }
}
