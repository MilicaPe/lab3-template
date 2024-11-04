package com.example.gateway.dto.error;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValidationErrorResponse extends Throwable {
    private String message;
    private List<ErrorDescription> descriptions;
}
