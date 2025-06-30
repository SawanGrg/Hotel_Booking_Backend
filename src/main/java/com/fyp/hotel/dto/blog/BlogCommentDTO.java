package com.fyp.hotel.dto.blog;

import com.fyp.hotel.model.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BlogCommentDTO {

    private long blogCommentId;
    private String comment;
    private User userName;
    private String createdDate;

    public BlogCommentDTO() {
    }

    public BlogCommentDTO(long blogCommentId, String comment, User userName, String createdDate) {
        this.blogCommentId = blogCommentId;
        this.comment = comment;
        this.userName = userName;
        this.createdDate = createdDate;
    }
}
