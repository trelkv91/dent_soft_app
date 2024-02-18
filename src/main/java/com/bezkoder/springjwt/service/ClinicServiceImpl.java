package com.bezkoder.springjwt.service;
import com.bezkoder.springjwt.dao.ClinicDAO;
import com.bezkoder.springjwt.models.Clinic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClinicServiceImpl implements ClinicService {

    private ClinicDAO clinicDAO;

    @Autowired
    public ClinicServiceImpl(ClinicDAO theClinicDAO) {
        clinicDAO = theClinicDAO;
    }

    @Override
    public List<Clinic> findAll() {
        return clinicDAO.findAll();
    }

    @Override
    public Clinic findById(int theId) {
        return clinicDAO.findById(theId);
    }

}






