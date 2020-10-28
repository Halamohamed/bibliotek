package com.example.bibliotek.repositories;

import com.example.bibliotek.entities.Users;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * The interface User repository.
 */
@Repository
public interface UserRepository extends MongoRepository<Users, String> {
    /**
     * Find user by username optional.
     *
     * @param username the username
     * @return the optional return one user have the same username
     */
    Optional<Users> findUserByUsername(String username);
}
