package com.fyp.hotel.dto.khalti;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.repository.cdi.Eager;

@Getter
@Setter
public class CustomerInfo {

    private String name;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;

    public CustomerInfo() {
    }

    public CustomerInfo(String userName, String firstName, String lastName, String email, String phone, String address) {
        this.name = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }

}
