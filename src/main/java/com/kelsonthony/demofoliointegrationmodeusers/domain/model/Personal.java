package com.kelsonthony.demofoliointegrationmodeusers.domain.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Embeddable
public class Personal {

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_id")
    private List<Address> addresses;

    @Column(nullable = false)
    private String firstName;

    private String middleName;

    @Column(nullable = false)
    private String lastName;

    private String preferredFirstName;

    @Column(nullable = false)
    private String dateOfBirth;

    @Column(nullable = false, unique = true)
    private String email;

    private String phone;

    private String mobilePhone;

    private String preferredContactTypeId;
}