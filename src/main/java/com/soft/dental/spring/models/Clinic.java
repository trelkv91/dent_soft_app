package com.soft.dental.spring.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "clinics")
public class Clinic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank
    @Size(max = 100)
    private String name;

    @Size(max = 100)
    private String company_name;

    @Size(max = 13)
    private String company_vat_number;

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private boolean active;

    @Size(max = 255)
    private String logo_path;
}
