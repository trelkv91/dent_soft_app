package com.soft.dental.spring.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.soft.dental.spring.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

  Optional<User> findByUsername(String username);

  Boolean existsByUsername(String username);

  Boolean existsByEmail(String email);

  //1. example
  @Query(value = "select u from User u where u.username = :userName")
  Optional<User> findByUsername2(@Param("userName") String username);

  //2. example
  @Query(value = "SELECT * from User", nativeQuery = true) //findAll
  List<User> findAllUsers();
}
