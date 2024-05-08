package com.fyp.hotel.dto.userDto;

import com.fyp.hotel.model.User;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;


import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@Builder
//@Data //data is a shortcut for @ToString, @EqualsAndHashCode, @Getter, @Setter and @RequiredArgsConstructor combined.
public class BlogDTO implements Serializable {

private static final long serialVersionUID = 1L;

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
