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

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * The type Book service.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class BookService {
    @Autowired
    private final BookRepository bookRepository;
    //private final UserService userService;


    /**
     * Find all list.
     *
     * @param name       the name of book
     * @param genre      the genre of books
     * @param author     the author
     * @param sortByName the sort by name
     * @param available  the available of books to loan
     * @return the list books
     */
    @Cacheable(value = "bookCache")
    public List<Books> findAll(String name, String genre,String author,boolean sortByName, boolean available){
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
        if(author != null){
            books = books.stream()
                    .filter(book -> book.getAuthor().toLowerCase().equals(author.toLowerCase()))
                    .collect(Collectors.toList());
        }
        if(sortByName){
            books.sort(Comparator.comparing(Books::getName));
        }
        if(available){
            books = books.stream()
                    .filter(book -> book.isAvailable())
                    .collect(Collectors.toList());
        }
        return books;
    }

    /**
     * Find by id book.
     *
     * @param id the book id
     * @return the book by id
     */
    @Cacheable(value = "bookCache", key = "#id")
    public Books findById(String id){
        return bookRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                String.format("book not found %s.", id)));
    }

    /**
     * Find by isbn books.
     *
     * @param isbn the isbn
     * @return the books
     */
    @Cacheable(value = "bookCache", key = "#isbn")
    public Books findByIsbn(String isbn){
        return bookRepository.findBooksByIsbn(isbn).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                String.format("book not found by isbn %s.", isbn)));
    }

    /**
     * Find by plot list.
     *
     * @param plot the plot
     * @return the list
     */
    @Cacheable(value = "bookCache", key = "#plot")
    public List<Books> findByPlot(String plot){
        return bookRepository.findAll().stream()
                .filter(p -> p.getPlot().toLowerCase().contains(plot))
                .collect(Collectors.toList());
    }

    /**
     * Save book.
     *
     * @param book the book to save
     * @return the book saved
     */
    @CachePut(value = "bookCache", key = "#result.id")
    public Books save(Books book){
        log.info("saving book.");
        return bookRepository.save(book);
    }

    /**
     * Update book.
     *
     * @param id   the book id
     * @param book the book to update
     */
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

    /**
     * Delete book.
     *
     * @param id the book id to delete
     */
    @CacheEvict(value = "bookCache", key = "#id")
    public void delete(String id){
        notExist(id, bookRepository.existsById(id), "Could not find book by id %s", "Could not find book by id %s");
        bookRepository.deleteById(id);
    }

    /**
     * Loan book if available from library .
     *
     * @param id   the book id
     * @param book the book to loan
     * @return the book if available, if not exaption
     */
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

    /**
     * Return book to library and make it available.
     *
     * @param id   the book id
     * @param book the book
     * @return the book returned
     */
    @CachePut(value = "bookCache", key = "#id")
    public Books returnBook(String id, Books book){
        log.info("return back a book.");
        notExist(id, bookRepository.existsById(id), "Could not find book by id %s", "not found book by id %s");

        var loanedBook = bookRepository.findById(id);
        loaned(id,!loanedBook.get().isAvailable(),"Book id is not available %s","Book not available by id %s");
      
        loanedBook.get().setId(id);
        loanedBook.get().setAvailable(true);
        bookRepository.save(loanedBook.get());
        return loanedBook.get();
    }


}
