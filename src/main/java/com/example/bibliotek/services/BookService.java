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
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;


    @Cacheable(value = "bookCache")
    public List<Books> findAll(String name, String genre, boolean isAvailable){
        log.info("Request to find all books");
        log.warn("Refresh to get books");
        var books = bookRepository.findAll();
        if(name != null){
            books = books.stream()
                    .filter(book -> book.getName().toLowerCase().startsWith(name.toLowerCase()))
                    .collect(Collectors.toList());
        }
        if(name != null){
            books = books.stream()
                    .filter(book -> book.getGenre().toLowerCase().equals(genre.toLowerCase()))
                    .collect(Collectors.toList());
        }
        if(isAvailable){
            books = books.stream()
                    .filter(book -> book.isAvailable())
                    .collect(Collectors.toList());
        }
        return books;
    }
    @Cacheable(value = "bookCache")
    public List<Books> findAll(){
        log.info("Request to find all books");
        log.warn("Refresh to get books");
        var books = bookRepository.findAll();
        return books;
    }

    @Cacheable(value = "bookCache", key = "#id")
    public Books findById(String id){
        return bookRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                String.format("book not found %s.", id)));
    }

    @CachePut(value = "bookCache", key = "#result.id")
    public Books save(Books book){
        log.info("saving book.");
        return bookRepository.save(book);
    }

    @CachePut(value = "bookCache", key = "#id")
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

    @CacheEvict(value = "bookCache", key = "#id")
    public void delete(String id){
        if(!bookRepository.existsById(id)){
            log.error(String.format("Could not find book by id %s", id));
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("Could not find book by id %s", id));
        }
        bookRepository.deleteById(id);
    }

}
