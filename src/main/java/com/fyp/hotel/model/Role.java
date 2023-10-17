package com.fyp.hotel.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Objects;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;


@RequiredArgsConstructor // generate constructor with all final fields as arguments
@Data
@Entity
@Table(name = "role")
public class Role {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="role_id")
    private Long roleId;

    @Column(name="role_name", nullable = false, unique = true)
    private String roleName;

    @Column(name="role_description", nullable = false)
    private String roleDescription;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.EAGER)
    private Set<User> user; 
    //sets defines a collection that cannot contain duplicate elements
    //for instance, if you have a set of integers, you can add 1, 2, 3, 4, 5, 6, 7, 8, 9, 10
    //but if you try to add 1 again, it will not be added to the set

    @Override
    public String toString() {
        return "Role{" +
                "roleId=" + roleId +
                ", roleName='" + roleName + '\'' +
                ", roleDescription='" + roleDescription + '\'' +
                '}';
    }
    @Override
    public int hashCode() {
        return Objects.hash(roleId, roleName); // Use only fields that uniquely identify a role
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Role)) return false;
        Role role = (Role) o;
        return Objects.equals(roleId, role.roleId) && Objects.equals(roleName, role.roleName);
    }

}
