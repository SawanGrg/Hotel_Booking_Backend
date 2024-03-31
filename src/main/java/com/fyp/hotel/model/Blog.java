package com.fyp.hotel.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "blog")
public class Blog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "blog_id")
    private Long blogId;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "blog_image", nullable = true, unique = false)
    private String blogImage;

    @Column(name = "blog_tag", nullable = true, unique = false)
    private String blogTag;

    @CreationTimestamp // Automatically set the date when the blog is created
    @Column(name = "created_date", nullable = false, updatable = false)
    private Date createdDate;

     @UpdateTimestamp // Automatically set the date when the blog is updated
    @Column(name = "updated_date", nullable = true)
    private Date updatedDate;

    // Many blogs can be associated with one user
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    // One blog can have many comments
    @JsonIgnore
    @JsonBackReference
    @OneToMany(mappedBy = "blog", cascade = CascadeType.ALL)
    private List<BlogComment> blogComments;

    public Blog() {
    }

    public Blog(String title, String description, String status, String blogImage, String blogTag, Date createdDate, Date updatedDate, User user) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.blogImage = blogImage;
        this.blogTag = blogTag;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
        this.user = user;
    }

}
