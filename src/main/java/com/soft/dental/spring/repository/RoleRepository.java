package com.soft.dental.spring.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.soft.dental.spring.models.ERole;
import com.soft.dental.spring.models.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
  Optional<Role> findByName(ERole name);
}
