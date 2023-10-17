package com.fyp.hotel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fyp.hotel.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUserName(String username);

    //native query for extracting all the user details and role name from user table and role table of a specific user name
    // @Query(value = "SELECT u.user_id, u.user_name, u.password, u.user_first_name, u.user_last_name, u.user_email, u.user_phone, u.user_address, r.role_name FROM user u INNER JOIN user_role ur ON u.user_id = ur.user_id INNER JOIN role r ON ur.role_id = r.role_id WHERE u.user_name = :username", nativeQuery = true)
    //  findUserAndRoleNameByUserName(@Param("username") String username);
 
}
