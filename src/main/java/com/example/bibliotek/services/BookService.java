package com.example.bibliotek.services;

import com.example.bibliotek.entities.Books;
import com.example.bibliotek.repositories.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookService {
    @Autowired
    private final BookRepository bookRepository;
    //private final UserService userService;


    @Cacheable(value = "bookCache")
    public List<Books> findAll(String name, String genre, boolean available){
        log.info("Request to find all books");
        log.warn("Refresh to get books");
        var books = bookRepository.findAll();
        if(name != null){
            books = books.stream()
                    .filter(book -> book.getName().toLowerCase().startsWith(name.toLowerCase()))
                    .collect(Collectors.toList());
        }
        if(genre != null){
            books = books.stream()
                    .filter(book -> book.getGenre().toLowerCase().equals(genre.toLowerCase()))
                    .collect(Collectors.toList());
        }
        if(available){
            books.stream()
                    .filter(book -> book.isAvailable() == available)
                    .collect(Collectors.toList());
        }
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
        notExist(id, bookRepository.existsById(id), "Could not find book by id %s", "not found book by id %s");
        book.setId(id);
        bookRepository.save(book);
    }

    private void notExist(String id, boolean b, String s, String s2) {
        if (!b) {
            log.error(String.format(s, id));
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format(s2, id));
        }
    }

    @CacheEvict(value = "bookCache", key = "#id")
    public void delete(String id){
        notExist(id, bookRepository.existsById(id), "Could not find book by id %s", "Could not find book by id %s");
        bookRepository.deleteById(id);
    }

    @CachePut(value = "bookCache", key = "#id")
    public Books loanBook( String id, Books book){
        log.info("loan a book.");
        notExist(id, bookRepository.existsById(id), "Could not find book by id %s", "not found book by id %s");
        var loanBook = bookRepository.findById(id);
        loaned(id, loanBook.get().isAvailable(), "Book id  not available %s", "not available book by id %s");
        loanBook.get().setId(id);
        loanBook.get().setAvailable(false);
        bookRepository.save(loanBook.get());
        return loanBook.get();
    }

    private void loaned(String id, boolean available, String s, String s2) {
        notExist(id, available, s, s2);
    }

    @CachePut(value = "bookCache", key = "#id")
    public Books returnBook(String id, Books book){
        //var id = book.getId();
        log.info("return back a book.");
        notExist(id, bookRepository.existsById(id), "Could not find book by id %s", "not found book by id %s");

        var loanedBook = bookRepository.findById(id);
        loaned(id,!loanedBook.get().isAvailable(),"Book id is not available %s","Book not available by id %s");
       /* if(!bookRepository.findBooksByIsAvailable(true).contains(book)){
            log.error(String.format("Book id  not available %s", id));
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("not available book by id %s", id));
        }*/
        loanedBook.get().setId(id);
        loanedBook.get().setAvailable(true);
        bookRepository.save(loanedBook.get());
        return loanedBook.get();
    }


}
