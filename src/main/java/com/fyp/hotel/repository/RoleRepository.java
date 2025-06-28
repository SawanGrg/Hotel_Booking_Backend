package com.fyp.hotel.repository;

import com.fyp.hotel.model.Role;
import com.fyp.hotel.model.User;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("roleRepository")
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByRoleName(String name);
    Set<Role> findRolesByUser(User user);
}
