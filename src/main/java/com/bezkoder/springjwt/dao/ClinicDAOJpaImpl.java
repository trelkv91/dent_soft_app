package com.bezkoder.springjwt.dao;

import com.bezkoder.springjwt.models.Clinic;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ClinicDAOJpaImpl implements ClinicDAO{

    // define field for entitymanager
    private EntityManager entityManager;


    // set up constructor injection
    @Autowired
    public ClinicDAOJpaImpl(EntityManager theEntityManager) {
        entityManager = theEntityManager;
    }


    @Override
    public List<Clinic> findAll() {

        // create a query
        TypedQuery<Clinic> theQuery = entityManager.createQuery("from Clinic", Clinic.class);

        // execute query and get result list
        List<Clinic> clinics = theQuery.getResultList();

        // return the results
        return clinics;
    }

    @Override
    public Clinic findById(int theId) {

        // get clinic
        Clinic theClinic = entityManager.find(Clinic.class, theId);

        // return clinic
        return theClinic;
    }

    @Override
    public Clinic save(Clinic theClinic) {

        // save clinic
        Clinic dbClinic = entityManager.merge(theClinic);

        // return the dbClinic
        return dbClinic;
    }

    @Override
    public void deleteById(int theId) {

        // find clinic by id
        Clinic theClinic = entityManager.find(Clinic.class, theId);

        // remove clinic
        entityManager.remove(theClinic);
    }
}