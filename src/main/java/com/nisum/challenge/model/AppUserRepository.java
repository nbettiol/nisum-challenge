package com.nisum.challenge.model;

import org.springframework.data.repository.CrudRepository;
import java.util.Optional;
import java.util.UUID;

public interface AppUserRepository extends CrudRepository<AppUser, UUID> {
    Optional<AppUser> findById(UUID id);
    Optional<AppUser> findByEmail(String email);
    Optional<AppUser> findByName(String name);
}