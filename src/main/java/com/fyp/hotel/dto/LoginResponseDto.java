package com.fyp.hotel.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponseDto {

    private String username;
    private String token;
    private String roleName;
    private String message;

    public LoginResponseDto() {
    }

    public LoginResponseDto(String username, String token, String role) {
        this.username = username;
        this.token = token;
        this.roleName = role;
    }




}
