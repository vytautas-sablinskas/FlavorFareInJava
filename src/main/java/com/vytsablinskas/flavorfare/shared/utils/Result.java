package com.vytsablinskas.flavorfare.shared.utils;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@Builder
public class Result {
    private boolean isSuccess;

    private String message;

    private HttpStatus statusCode;
}
