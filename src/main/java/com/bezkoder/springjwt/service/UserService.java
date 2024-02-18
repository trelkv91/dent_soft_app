package com.bezkoder.springjwt.service;

import com.bezkoder.springjwt.models.User;

import java.util.List;

public interface UserService {

    List<User> findAll();

    User findById(int theId);

    User save(User theUser);

    void deleteById(int theId);
}