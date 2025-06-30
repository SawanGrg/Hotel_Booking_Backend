package com.fyp.hotel.dto.user;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public class UserChangePasswordDto {
    @NotNull(message = "Old password cannot be null")
    @Length(min = 5, max = 50, message = "Old password must be between 5 and 12 characters")
    private String oldPassword;

    @NotNull(message = "New password cannot be null")
    @Length(min = 5, max = 50, message = "New password must be between 5 and 12 characters")
    private String newPassword;
    public UserChangePasswordDto() {
    }

    public UserChangePasswordDto(String oldPassword, String newPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
