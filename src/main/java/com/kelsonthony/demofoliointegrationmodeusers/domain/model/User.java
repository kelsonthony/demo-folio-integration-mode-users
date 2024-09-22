package com.kelsonthony.demofoliointegrationmodeusers.domain.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "folio_user", schema = "foliouserschema")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private boolean active;

    @Embedded
    private Personal personal;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String patronGroup;

    @Column(nullable = false)
    private String expirationDate;

    @Column(unique = true)
    private String barcode;

    @Column(nullable = false)
    private String enrollmentDate;

    @Column(unique = true)
    private String externalSystemId;

    @ElementCollection
    @CollectionTable(name = "user_departments", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "department")
    private List<String> departments;
}