package com.kelsonthony.demofoliointegrationmodeusers.app.dto;

import lombok.Data;

@Data
public class UserExcelDTO {
    private boolean active;
    private String firstName;
    private String middleName;
    private String lastName;
    private String preferredFirstName;
    private String dateOfBirth;
    private String email;
    private String phone;
    private String mobilePhone;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String region;
    private String postalCode;
    private String addressTypeId;
    private String countryId;
    private String preferredContactTypeId;
    private String username;
    private String patronGroup;
    private String expirationDate;
    private String barcode;
    private String enrollmentDate;
    private String externalSystemId;
    private String departments;
}