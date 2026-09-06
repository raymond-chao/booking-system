package com.raymond.bookingsystem.DTO;

//Det är bara ett paket med data som Bookning service kan skicka via REST till Customer service
public record CreateCustomerRequest(
        String name,
        String email,
        String phoneNumber,
        String password
) {
}
