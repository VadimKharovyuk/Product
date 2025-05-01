package com.example.product.util;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
public class ValidationError extends ApiError {
    private Map<String, String> fieldErrors;

    public ValidationError(String code, String message, Map<String, String> fieldErrors) {
        super(code, message);
        this.fieldErrors = fieldErrors;
    }
}