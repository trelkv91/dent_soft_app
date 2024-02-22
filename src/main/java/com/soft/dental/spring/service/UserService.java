package com.soft.dental.spring.service;

import com.soft.dental.spring.models.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> findAll();

    Optional<User> findById(int theId);

    User findByUsername(String username);

    User save(User theUser);

    void deleteById(int theId);
}