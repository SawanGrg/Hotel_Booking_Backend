package com.fyp.hotel.dto.userDto;

import com.fyp.hotel.model.Hotel;
import com.fyp.hotel.model.User;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class IssueReportDTO {

    private String title;
    private String description;
    private User user;
    private String status;
    private Hotel hotel;
    private Date createdDate;

    public IssueReportDTO() {
    }

    public IssueReportDTO(String title, String description, User user, String status, Hotel hotel, Date createdDate) {
        this.title = title;
        this.description = description;
        this.user = user;
        this.status = status;
        this.hotel = hotel;
        this.createdDate = createdDate;
    }


}
