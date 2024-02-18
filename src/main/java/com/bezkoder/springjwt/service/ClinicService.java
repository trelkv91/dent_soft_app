package com.bezkoder.springjwt.service;

import com.bezkoder.springjwt.models.Clinic;

import java.util.List;

public interface ClinicService {

    List<Clinic> findAll();

    Clinic findById(int theId);
}