package com.book.bookrating.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class ResourceNotFoundException {
    private String errorMessage;
    public ResourceNotFoundException() {
    }

    public ResourceNotFoundException(String message) {
        this.errorMessage = message;

    }

    public String getErrorMessage() {
        return errorMessage;
    }

}
