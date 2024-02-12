package com.fyp.hotel.dto.userDto;

import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class UserProfileDto {

    @Nullable
    private String userName;

    @Nullable
    private String userFirstName;

    @Nullable
    private String userLastName;

    @Nullable
    private String userEmail;

    @Nullable
    private String userPhone;

    @Nullable
    private String userAddress;

    @Nullable
    private String dateOfBirth;

    public UserProfileDto() {
    }

    public UserProfileDto(@Nullable String userName, @Nullable String userFirstName, @Nullable String userLastName, @Nullable String userEmail, @Nullable String userPhone, @Nullable String userAddress, @Nullable String dateOfBirth) {
        this.userName = userName;
        this.userFirstName = userFirstName;
        this.userLastName = userLastName;
        this.userEmail = userEmail;
        this.userPhone = userPhone;
        this.userAddress = userAddress;
        this.dateOfBirth = dateOfBirth;
    }
}
