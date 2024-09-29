package com.kelsonthony.demofoliointegrationmodeusers.app.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UserDTO {
    private boolean active;
    private PersonalDTO personal;
    private String username;
    private String patronGroup;
    private String expirationDate;
    private String barcode;
    private String enrollmentDate;
    private String externalSystemId;
    private List<String> departments = new ArrayList<>();


}