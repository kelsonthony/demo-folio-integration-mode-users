package com.kelsonthony.demofoliointegrationmodeusers.domain.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "folio_addresses", schema = "foliouserschema")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String addressLine1;

    private String addressLine2;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String region;

    @Column(nullable = false)
    private String postalCode;

    @Column(nullable = false)
    private String addressTypeId;

    @Column(nullable = false)
    private String countryId;
}