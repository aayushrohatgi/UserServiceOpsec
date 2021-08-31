package com.opsec.userservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.BAD_REQUEST, reason="Date of birth must be in dd/MM/yyyy format")  // 404
public class UnsupportedDOBDateFormatException extends Exception {
    public UnsupportedDOBDateFormatException(String message) {
        super(message);
    }
}
