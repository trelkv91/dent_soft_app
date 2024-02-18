package com.bezkoder.springjwt.dao;

import com.bezkoder.springjwt.models.Clinic;

import java.util.List;

public interface ClinicDAO {

    List<Clinic> findAll();

    Clinic findById(int theId);

    Clinic save(Clinic theClinic);

    void deleteById(int theId);
}










