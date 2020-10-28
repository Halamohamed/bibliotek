package com.example.bibliotek.controller;

import com.example.bibliotek.entities.Books;
import com.example.bibliotek.services.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The type Book controller.
 */
@RestController
@RequestMapping("/api/books")
@Slf4j
public class BookController {

    @Autowired
    private BookService bookService;


    /**
     * Find all books response entity.
     *
     * @param name        the name
     * @param genre       the genre
     * @param isAvailable the is available
     * @return the response entity
     */
    @GetMapping
    public ResponseEntity<List<Books>> findAllBooks(@RequestParam(required = false) String name,
                                                    @RequestParam(required = false) String genre,
                                                    @RequestParam(required = false) boolean isAvailable){
        log.info("getting books with name " + name);
        log.warn("Refresh to get books");
        return ResponseEntity.ok(bookService.findAll(name,genre, isAvailable));
    }

    /**
     * Find book by id response entity.
     *
     * @param id the id
     * @return the response entity
     */
    @Secured({"ROLE_ADMIN","ROLE_USER","ROLE_EDITOR"})
    @GetMapping("/{id}")
    public ResponseEntity<Books> findBookById(@PathVariable String id){
        return ResponseEntity.ok(bookService.findById(id));
    }

    /**
     * Save book response entity.
     *
     * @param book the book
     * @return the response entity
     */
    @Secured({"ROLE_ADMIN","ROLE_EDITOR"})
    @PostMapping
    public ResponseEntity<Books> saveBook(@Validated @RequestBody Books book){
        return ResponseEntity.ok(bookService.save(book));
    }

    /**
     * Update book.
     *
     * @param id   the id
     * @param book the book
     */
    @Secured({"ROLE_ADMIN","ROLE_EDITOR"})
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateBook(@PathVariable String id, @Validated @RequestBody Books book) {
        bookService.update(id, book);
    }

    /**
     * Delete book.
     *
     * @param id the id
     */
    @Secured({"ROLE_ADMIN","ROLE_EDITOR"})
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBook(@PathVariable String id) {
        bookService.delete(id);
    }

    /**
     * Loan book.
     *
     * @param id   the id
     * @param book the book
     */
    @Secured({"ROLE_ADMIN","ROLE_USER","ROLE_EDITOR"})
    @PutMapping("/loan/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void loanBook( @PathVariable String id, @Validated @RequestBody Books book) { //@PathVariable String id
        bookService.loanBook(id, book);
    }

    /**
     * Return book.
     *
     * @param id   the id
     * @param book the book
     */
    @Secured({"ROLE_ADMIN","ROLE_USER","ROLE_EDITOR"})
    @PutMapping("/return/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void returnBook(@PathVariable String id,  @Validated @RequestBody Books book) {
        bookService.returnBook(id,book);
    }




}
