package com.fyp.hotel.dto.blog;

import com.fyp.hotel.model.User;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;


import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@Builder
public class BlogDTO {
    private long id;

    @NotEmpty(message = "Title is required")
    @Length(min = 20, message = "Title must be at least 20 characters")
    private String title;

    @NotEmpty(message = "Description is required")
    @Length(min = 100, message = "Description must be at least 100 characters")
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
