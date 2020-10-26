package com.example.bibliotek.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class Users implements Serializable {
    private static final long serialVersionUID = 2L;
    @Id
    private String id;
    @NotEmpty(message = "Firstname can not be empty")
    @Size(min = 3, max = 12, message = "Firstname length invalid")
    private String firstName;
    @NotEmpty(message = "Lastname can not be empty")
    @Size(min = 3, max = 12, message = "Lastname length invalid")
    private String lastName;
    @Past(message = "Birthday can not be present or in the future")
    @NotNull(message = "Birthday can not be empty")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
    private LocalDate birthdate;
    @Email(message = "E-mail address invalid")
    private String mail;
    @Pattern(regexp = "([0-9]){2,4}-([0-9]){5,8}", message = "Phone number invalid")
    private String phone;
    @Size(min = 4, max = 10, message = "Username length invalid")
    @NotBlank(message = "Username must contain a value")
    @Indexed(unique = true)
    private String username;
    @Size(min = 4, max = 10, message = "Password length invalid")
    @NotBlank(message = "Password must contain a value")
    private String password;
    private List<Roles> roles;

    public Users() {
    }
}
