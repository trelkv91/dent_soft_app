package com.soft.dental.spring.service;
import com.soft.dental.spring.models.User;
import com.soft.dental.spring.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findById(int theId) {
        return userRepository.findById(theId);
    }

    @Override
    //TODO Exception
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    @Transactional
    @Override
    public User save(User theUser) {
        return userRepository.save(theUser);
    }

    @Transactional
    @Override
    public void deleteById(int theId) {
        userRepository.deleteById(theId);
    }
}






