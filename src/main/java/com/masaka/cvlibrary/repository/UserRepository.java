package com.masaka.cvlibrary.repository;

import com.masaka.cvlibrary.model.User;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends Repository<User, UUID> {
    Optional<User> findByEmail(String email);
    List<User> findAll();
    User save(User user);
    Optional<User> findById(UUID id);
    void delete(User user);
}
