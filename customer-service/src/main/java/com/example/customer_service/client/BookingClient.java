package com.example.customer_service.client;

import com.example.customer_service.client.BookingClient;

import com.example.customer_service.error.ServiceUnavailableException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Component
public class BookingClient {

    private final RestClient restClient;
    //private final BookingClient bookingClient;

    public BookingClient(@Value("${booking-service.url}") String baseUrl) {
        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

//    public boolean hasActiveBookings(String email) {
//        return restClient.get()
//                .uri("/api/bookings/customer/{email}/active", email)
//                .retrieve()
//                .body(Boolean.class);
//    }


    public boolean hasActiveBookings(String email) {
        try {
            Boolean result = restClient.get()
                    .uri("/api/bookings/customer/{email}/active", email)
                    .retrieve()
                    .body(Boolean.class);

            return Boolean.TRUE.equals(result);

        } catch (RestClientException e) {
            throw new ServiceUnavailableException(
                    "Booking service is not available, try again later"
            );
        }
    }

}