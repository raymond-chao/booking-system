package com.example.customer_service.model;

public record CreateCustomerRequest(
        String name,
        String email,
        String phoneNumber,
        String password
) {
}