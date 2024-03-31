package com.fyp.hotel.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "blog_comment")
public class BlogComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long commentId;

    @Column(name = "comment", nullable = false, columnDefinition = "TEXT")
    private String comment;

    @Column(name = "status", nullable = true)
    private String status;

    @Column(name ="created_at", nullable = false)
    private LocalDate createdDate;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "blog_id", nullable = false)
    private Blog blog;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public BlogComment() {
    }

    public BlogComment(String comment, String status, Blog blog, User user) {
        this.comment = comment;
        this.status = status;
        this.blog = blog;
        this.user = user;
    }

    @Override
    public String toString() {
        return "BlogComment{" +
                "commentId=" + commentId +
                ", comment='" + comment + '\'' +
                ", status='" + status + '\'' +
                ", blog=" + blog +
                ", user=" + user +
                '}';
    }
}
