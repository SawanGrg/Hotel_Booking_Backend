package com.fyp.hotel.dto.khalti;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fyp.hotel.model.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KhaltiInitationRequest {

    private String return_url;
    private String website_url;
    private long amount;
    private String purchase_order_id;
    private String purchase_order_name;

    @JsonProperty("customer_info")
    private CustomerInfo customerInfo; // Change the field name to follow Java naming conventions

    public KhaltiInitationRequest() {
    }

    public KhaltiInitationRequest(String return_url, String website_url, long amount, String purchase_order_id, String purchase_order_name,
                                  CustomerInfo customerInfo) {
        this.return_url = return_url;
        this.website_url = website_url;
        this.amount = amount;
        this.purchase_order_id = purchase_order_id;
        this.purchase_order_name = purchase_order_name;
        this.customerInfo = customerInfo;
    }
}
