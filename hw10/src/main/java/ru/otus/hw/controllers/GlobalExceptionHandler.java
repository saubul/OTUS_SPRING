package ru.otus.hw.controllers;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.otus.hw.dto.ErrorDto;
import ru.otus.hw.exceptions.NotFoundException;

import java.util.logging.Level;
import java.util.logging.Logger;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = Logger.getLogger("ERROR_LOGGER");

    @ExceptionHandler(NotFoundException.class)
    public ErrorDto handleNotFoundException(NotFoundException ex) {
        LOGGER.log(Level.INFO, ex.getMessage());
        return new ErrorDto(ex.getMessage());
    }

}
