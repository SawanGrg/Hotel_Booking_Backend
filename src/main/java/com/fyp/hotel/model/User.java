package com.fyp.hotel.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor // generate constructor with all final fields as arguments
@Data
@Entity
@Table(name = "user")
public class 
User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "user_name", nullable = false, unique = true)
    private String userName;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "user_first_name", nullable = false)
    private String userFirstName;

    @Column(name = "user_last_name", nullable = false)
    private String userLastName;

    @Column(name = "user_email", nullable = false, unique = true)
    private String userEmail;

    @Column(name = "user_phone", nullable = true)
    private String userPhone;

    @Column(name = "user_address", nullable = true)
    private String userAddress;

    @Column(name = "user_profile_picture", nullable = true)
    private String userProfilePicture;

    @Column(name = "date_of_birth", nullable = true)
    private String dateOfBirth;

    @Column(name = "user_status", nullable = false)
    private String userStatus;

    @Column(name = "created_at", length = 20, nullable = true)
    private Instant createdAt;

    @Column(name = "updated_at", length = 20, nullable = true)
    private Instant updatedAt;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @OneToOne( mappedBy = "user",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Hotel hotel;
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        
        System.out.println("Roles from the user:----> before " + roles);

        for (Role userRole : roles) {
            System.out.println("Role name from user role: " + userRole.getRoleName());
            authorities.add(new SimpleGrantedAuthority(userRole.getRoleName()));
        }
    
        System.out.println("Authorities from the user: " + authorities);

        return authorities;
    }
    
    @Override
    public String getUsername() {
        return this.userName;
    }

    @Override
    public boolean isAccountNonExpired() {

        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    //overriding the toString() method
    @Override
    public String toString() {
        return "User [userId=" + userId + ", userName=" + userName + ", password=" + password + ", userFirstName="
                + userFirstName + ", userLastName=" + userLastName + ", userEmail=" + userEmail + ", userPhone="
                + userPhone + ", userAddress=" + userAddress + ", userProfilePicture=" + userProfilePicture
                + ", dateOfBirth=" + dateOfBirth + ", userStatus=" + userStatus + ", createdAt=" + createdAt
                + ", updatedAt=" + updatedAt + ", roles=" + roles + "]";
    }
    @Override
public int hashCode() {
    return Objects.hash(userId, userName); // Use only fields that uniquely identify a user
}

@Override
public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof User)) return false;
    User user = (User) o;
    return Objects.equals(userId, user.userId) && Objects.equals(userName, user.userName);
}

}
