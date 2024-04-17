package com.fyp.hotel.dto.admin;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class AdminRevenueDTO  implements Serializable {
    private static final long serialVersionUID = 1L;

    private long cashOnArrival;
    private long khaltiPayment;

    public AdminRevenueDTO(long cashOnArrival, long khaltiPayment) {
        this.cashOnArrival = cashOnArrival;
        this.khaltiPayment = khaltiPayment;
    }

    public AdminRevenueDTO() {
    }
}
