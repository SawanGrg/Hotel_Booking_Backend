package com.fyp.hotel.repository;

import com.fyp.hotel.model.BlogComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BlogCommentRepository extends JpaRepository<BlogComment, Long> {

    @Query("SELECT c from BlogComment c JOIN FETCH c.user u JOIN FETCH c.blog b WHERE b.blogId = :blog_id")
    List<BlogComment> findAllByBlogId(@Param("blog_id") long blogId);
}

