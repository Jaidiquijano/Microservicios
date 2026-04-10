package com.aula.microservices.common.response;
public record ApiResponse<T>(
        boolean success,
        String message,
        T data
) {
}