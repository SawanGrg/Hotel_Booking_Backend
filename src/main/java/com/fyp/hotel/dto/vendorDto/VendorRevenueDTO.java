package com.fyp.hotel.dto.vendorDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VendorRevenueDTO {

    private long cashOnArrival;
    private long khaltiPayment;

    public VendorRevenueDTO(long cashOnArrival, long khaltiPayment) {
        this.cashOnArrival = cashOnArrival;
        this.khaltiPayment = khaltiPayment;
    }

    public VendorRevenueDTO() {
    }
}
