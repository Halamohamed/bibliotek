package com.example.bibliotek.repositories;

import com.example.bibliotek.entities.Books;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends MongoRepository<Books, String> {

    List<Books> findBooksByIsAvailable(boolean available);

   // Collection<Object> findBooksByIsAvailable(boolean b);
}
