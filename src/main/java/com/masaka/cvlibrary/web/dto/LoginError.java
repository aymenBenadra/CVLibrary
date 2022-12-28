package com.masaka.cvlibrary.web.dto;

import org.springframework.http.HttpStatus;

public record LoginError(String message, HttpStatus status) {
}
