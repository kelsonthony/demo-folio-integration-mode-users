package com.kelsonthony.demofoliointegrationmodeusers.app.dto;

import lombok.Data;

@Data
public class AddressDTO {
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String region;
    private String postalCode;
    private String addressTypeId;
    private String countryId;
}
