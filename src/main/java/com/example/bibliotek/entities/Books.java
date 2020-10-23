package com.example.bibliotek.entities;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
@Builder
public class Books {

    @Id
    private String id;
    private String name;
    private String isbn;
    private String plot;
    private String author;
    private String genre;
    private boolean isAvailable;

    //String id, String isbn, String name, String plot, String author,
    //String genre, boolean isAvailable.
}
