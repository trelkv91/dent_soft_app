package com.bezkoder.springjwt.dao;

import com.bezkoder.springjwt.models.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDAOJpaImpl implements UserDAO {

    // define field for entitymanager
    private EntityManager entityManager;


    // set up constructor injection
    @Autowired
    public UserDAOJpaImpl(EntityManager theEntityManager) {
        entityManager = theEntityManager;
    }


    @Override
    public List<User> findAll() {

        // create a query
        TypedQuery<User> theQuery = entityManager.createQuery("from User", User.class);

        // execute query and get result list
        List<User> users = theQuery.getResultList();

        // return the results
        return users;
    }

    @Override
    public User findById(int theId) {

        // get user
        User theUser = entityManager.find(User.class, theId);

        // return user
        return theUser;
    }

    @Override
    public User save(User theUser) {

        // save user
        User dbUser = entityManager.merge(theUser);

        // return the dbUser
        return dbUser;
    }

    @Override
    public void deleteById(int theId) {

        // find user by id
        User theUser = entityManager.find(User.class, theId);

        // remove user
        entityManager.remove(theUser);
    }
}