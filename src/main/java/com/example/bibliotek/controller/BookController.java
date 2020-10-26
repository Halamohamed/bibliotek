package com.example.bibliotek.controller;

import com.example.bibliotek.entities.Books;
import com.example.bibliotek.services.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books/")
@Slf4j
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping
    public ResponseEntity<List<Books>> findAllBooks(@RequestParam(required = false) String name,
                                                    @RequestParam(required = false) String genre,
                                                    @RequestParam(required = false) boolean isAvailable){
        return ResponseEntity.ok(bookService.findAll(name,genre, isAvailable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Books> findBookById(@PathVariable String id){
        return ResponseEntity.ok(bookService.findById(id));
    }


    @PostMapping
    public ResponseEntity<Books> saveBook(@Validated @RequestBody Books book){
        return ResponseEntity.ok(bookService.save(book));
    }
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateBook(@PathVariable String id, @Validated @RequestBody Books book) {
        bookService.update(id, book);
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBook(@PathVariable String id) {
        bookService.delete(id);
    }






}
