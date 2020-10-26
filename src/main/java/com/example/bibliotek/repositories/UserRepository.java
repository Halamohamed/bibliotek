package com.example.bibliotek.repositories;

import com.example.bibliotek.entities.Users;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<Users, String> {
    Optional<Users> findUserByUsername(String username);
}
