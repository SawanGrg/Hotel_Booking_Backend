package com.fyp.hotel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fyp.hotel.model.User;

@Repository("userRepository") //to avoid bean conflict with user service
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUserName(String username);
 
}
