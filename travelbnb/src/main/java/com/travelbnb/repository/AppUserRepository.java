package com.travelbnb.repository;

import com.travelbnb.Entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {
  boolean existsByEmail(String email);

  boolean existsByUsername(String username);

  Optional<AppUser> findByUsername(String username);
}