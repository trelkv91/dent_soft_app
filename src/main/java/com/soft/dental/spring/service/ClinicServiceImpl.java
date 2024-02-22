package com.soft.dental.spring.service;
import com.soft.dental.spring.models.Clinic;
import com.soft.dental.spring.repository.ClinicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClinicServiceImpl implements ClinicService {

    private ClinicRepository clinicRepository;

    @Autowired
    public ClinicServiceImpl(ClinicRepository theClinicRepository) {
        clinicRepository = theClinicRepository;
    }

    @Override
    public Clinic save(Clinic theClinic) {
        return clinicRepository.save(theClinic);
    }

    @Override
    public List<Clinic> findAll() {
        return clinicRepository.findAll();
    }

    @Override
    public Optional<Clinic> findById(int theId) {
        return clinicRepository.findById(theId);
    }

}






