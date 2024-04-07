package com.fyp.hotel.dto.vendorDto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class VendorRevenueDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private long cashOnArrival;
    private long khaltiPayment;

    public VendorRevenueDTO(long cashOnArrival, long khaltiPayment) {
        this.cashOnArrival = cashOnArrival;
        this.khaltiPayment = khaltiPayment;
    }

    public VendorRevenueDTO() {
    }
}
