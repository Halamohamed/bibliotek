package com.example.bibliotek.repositories;

import com.example.bibliotek.entities.Books;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * The interface Book repository.
 */
@Repository
public interface BookRepository extends MongoRepository<Books, String> {

    /**
     * Find books by is available list.
     *
     * @param available the available if books is available to loan
     * @return the list of books available
     */
    List<Books> findBooksByIsAvailable(boolean available);

    Optional<Books> findBooksByIsbn(String isbn);

    List<Books> findBooksByPlotContains(String plot);
}
