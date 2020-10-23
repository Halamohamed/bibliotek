package com.example.bibliotek.services;

import com.example.bibliotek.entities.Books;
import com.example.bibliotek.repositories.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;

    @Cacheable(value = "bookCache")
    public List<Books> findAll(){
        log.info("Request to find all books");
        return bookRepository.findAll();
    }

    @Cacheable
    public Books findById(String id){
        return bookRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                String.format("book not found %s.", id)));
    }

    @CachePut
    public Books save(Books book){
        log.info("saving book.");
        return bookRepository.save(book);
    }

    @CachePut
    public void update(String id, Books book){
        log.info("update a book.");
        if(!bookRepository.existsById(id)){
            log.error(String.format("Could not find book by id %s", id));
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("not found book by id %s", id));
        }
        book.setId(id);
        bookRepository.save(book);
    }

    @CacheEvict
    public void delete(String id){
        if(!bookRepository.existsById(id)){
            log.error(String.format("Could not find book by id %s", id));
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("Could not find book by id %s", id));
        }
        bookRepository.deleteById(id);
    }

}
