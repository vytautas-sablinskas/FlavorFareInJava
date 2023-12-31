package com.vytsablinskas.flavorfare.business.exceptions.handlers;

import com.vytsablinskas.flavorfare.business.exceptions.TableSizeAlreadyInDatabaseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class TableSizeAlreadyInDatabaseExceptionHandler {
    @ExceptionHandler(TableSizeAlreadyInDatabaseException.class)
    public ResponseEntity<String> handleTableSizeAlreadyInDatabaseException(TableSizeAlreadyInDatabaseException ex) {
        return new ResponseEntity<String>(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
    }
}