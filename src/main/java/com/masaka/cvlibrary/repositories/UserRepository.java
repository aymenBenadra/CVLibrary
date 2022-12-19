package com.masaka.cvlibrary.repositories;

import com.masaka.cvlibrary.models.User;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends Repository<User, UUID> {
    List<User> findAll();
    User save(User user);
    Optional<User> findById(UUID id);
    void delete(User user);
}
