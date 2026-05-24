package com.raymond.bookingsystem.model;

public record CreateCustomerRequest(String name, String email, String phoneNumber, String password) {

}
