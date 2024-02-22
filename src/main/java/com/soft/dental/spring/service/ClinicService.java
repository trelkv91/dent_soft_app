package com.soft.dental.spring.service;

import com.soft.dental.spring.models.Clinic;

import java.util.List;
import java.util.Optional;

public interface ClinicService {

    Clinic save(Clinic theClinic);

    List<Clinic> findAll();

    Optional<Clinic> findById(int theId);
}