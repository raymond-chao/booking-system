package com.raymond.bookingsystem.client;

import com.raymond.bookingsystem.error.ServiceUnavailableException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import com.raymond.bookingsystem.DTO.CreateCustomerRequest;

@Component
public class CustomerClient {
    private final RestClient restClient;

    public CustomerClient(@Value("${customer-service.url}") String baseUrl) {
        this.restClient = RestClient.builder().baseUrl(baseUrl).build();
    }


    public boolean customerExists(String email) {
        try {
            restClient.get().uri("/api/customers/email/{email}", email).retrieve().toBodilessEntity();
            return true;
        } catch (HttpClientErrorException e) {
            return false;
        } catch (RestClientException e) {
            throw new ServiceUnavailableException("Kundtjänst inte tillgänglig, försök igen senare"
            );
        }



    }

    //skapa kund i CustomerService med DTO
    public void createCustomer(CreateCustomerRequest request) {
        try {
            restClient.post()
                    .uri("/api/customers")
                    .body(request)
                    .retrieve()
                    .toBodilessEntity();

        } catch (RestClientException e) {
            throw new ServiceUnavailableException(
                    "Kundtjänst inte tillgänglig, försök igen senare"
            );
        }
    }
}
