package com.kelsonthony.demofoliointegrationmodeusers.app.dto;

import lombok.Data;

import java.util.List;

@Data
public class PersonalDTO {
    private List<AddressDTO> addresses;
    private String firstName;
    private String middleName;
    private String lastName;
    private String preferredFirstName;
    private String dateOfBirth; // Formato esperado: "YYYY-MM-DD"
    private String email;
    private String phone;
    private String mobilePhone;
    private String preferredContactTypeId;
}
