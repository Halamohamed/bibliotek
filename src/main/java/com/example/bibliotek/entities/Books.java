package com.example.bibliotek.entities;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@Builder
public class Books implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private String id;
    @NotEmpty(message = "Book name can not be empty")
    @Size(min = 3, max = 20, message = "Book name length invalid")
    private String name;
    @NotBlank(message = "Isbn must contain a value")
    @Size(min = 5, max = 20, message = "Isbn length invalid")
    @Indexed(unique = true)
    private String isbn;
    private String plot;
    @NotEmpty(message = "Author can not be empty")
    private String author;
    private String genre;
    private boolean isAvailable;


}
