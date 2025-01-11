package com.fyp.hotel.dto.userDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserMessageDTO {

    private Long messageId;
    private String firstName;
    private String lastName;
    private String email;
    private String message;
    private String createdAt;

    public UserMessageDTO() {
    }

    public UserMessageDTO(Long messageId, String firstName, String lastName, String email, String message, String createdAt) {
        this.messageId = messageId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.message = message;
        this.createdAt = createdAt;
    }
}
