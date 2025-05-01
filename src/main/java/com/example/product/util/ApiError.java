package com.example.product.util;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiError {
    private String code;
    private String message;
    private LocalDateTime timestamp = LocalDateTime.now();

    public ApiError(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
