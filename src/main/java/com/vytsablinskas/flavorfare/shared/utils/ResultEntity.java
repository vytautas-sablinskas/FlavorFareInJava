package com.vytsablinskas.flavorfare.shared.utils;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@Builder
public class ResultEntity<T> {
    private boolean isSuccess;

    private String message;

    private HttpStatus statusCode;

    T entity;
}