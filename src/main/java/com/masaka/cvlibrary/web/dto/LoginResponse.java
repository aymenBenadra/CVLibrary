package com.masaka.cvlibrary.web.dto;

public record LoginResponse(String token, String name, String email, String role) {
}
