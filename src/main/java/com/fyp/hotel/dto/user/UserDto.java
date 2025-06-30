package com.fyp.hotel.dto.user;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class UserDto {

    private Long userId;

    @NotNull(message = "User name cannot be null")
    @Length(min = 4, max = 50, message = "User name must be between 4 and 50 characters")
    private String userName;

    @NotNull(message = "Password cannot be null")
    @Length(min = 5, max = 50, message = "Password must be between 8 and 50 characters")
    private String password;

    @NotNull(message = "First name cannot be null")
    private String userFirstName;

    @NotNull(message = "Last name cannot be null")
    private String userLastName;

    @NotNull(message = "Email cannot be null")
    @Email(message = "Invalid email format")
    private String userEmail;

    @Pattern(regexp = "^(\\d{10})?$", message = "Invalid phone number format. Use 10 digits.")
    private String userPhone;

    @NotNull(message = "Address cannot be null")
    private String userAddress;

    @NotNull(message = "Date of birth cannot be null")
    private String dateOfBirth;

    private String userProfilePicture;
    private String userStatus;

    public UserDto() {
    }

    public UserDto(String userName,String userPassword, String userFirstName, String userLastName, String userEmail,
                   String userPhone, String userAddress, String userProfilePicture, String dateOfBirth, String userStatus) {
        this.userName = userName;
        this.password = userPassword;
        this.userFirstName = userFirstName;
        this.userLastName = userLastName;
        this.userEmail = userEmail;
        this.userPhone = userPhone;
        this.userAddress = userAddress;
        this.userProfilePicture = userProfilePicture;
        this.dateOfBirth = dateOfBirth;
        this.userStatus = userStatus;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserPassword(String userPassword) {
        this.password = userPassword;
    }

    public String getUserPassword() {
        return this.password;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public String getUserProfilePicture() {
        return userProfilePicture;
    }

    public void setUserProfilePicture(String userProfilePicture) {
        this.userProfilePicture = userProfilePicture;
    }

    public String getUserDateOfBirth() {
        return dateOfBirth;
    }

    public void setUserDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    @Override
    public String toString() {
        return "UserDto [userId=" + userId + ", userName=" + userName + ", userFirstName=" + userFirstName +
                ", userLastName=" + userLastName + ", userEmail=" + userEmail + ", userPhone=" + userPhone +
                ", userAddress=" + userAddress + ", userProfilePicture=" + userProfilePicture + ", dateOfBirth=" +
                dateOfBirth + ", userStatus=" + userStatus + "]";
    }
}

