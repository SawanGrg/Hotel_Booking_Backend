package com.fyp.hotel.dto.userDto;

import com.fyp.hotel.model.User;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
public class BlogDTO implements Serializable {

private static final long serialVersionUID = 1L;

    private long id;

    @NotEmpty(message = "Title is required")
    private String title;

    @NotEmpty(message = "Description is required")
    private String description;

    @NotEmpty(message = "Blog tag is required")
    private String blogTag;

    private String blogImage;

    private String status;

    private Date createdDate;

    private User user;

    public BlogDTO() {
    }

    public BlogDTO(long id, String title, String description, String blogTag, String blogImage, String status, Date createdDate, User user) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.blogTag = blogTag;
        this.blogImage = blogImage;
        this.status = status;
        this.createdDate = createdDate;
        this.user = user;
    }



}
