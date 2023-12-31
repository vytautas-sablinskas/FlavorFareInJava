package com.vytsablinskas.flavorfare.business.exceptions;

public class TableSizeAlreadyInDatabaseException extends RuntimeException {
    public TableSizeAlreadyInDatabaseException(String message) { super(message); }
}