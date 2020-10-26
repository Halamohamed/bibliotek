package com.example.bibliotek.entities.validations;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EntityError {
    private String field;
    private String message;
    private String rejectedValue;
}
