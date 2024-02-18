package com.bezkoder.springjwt.dao;

import com.bezkoder.springjwt.models.User;

import java.util.List;

public interface UserDAO {

    List<User> findAll();

    User findById(int theId);

    User save(User theUser);

    void deleteById(int theId);
}










