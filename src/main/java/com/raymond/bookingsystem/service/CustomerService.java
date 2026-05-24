package com.raymond.bookingsystem.service;

import com.raymond.bookingsystem.model.CreateCustomerRequest;
import com.raymond.bookingsystem.repository.*;
import com.raymond.bookingsystem.model.Customer;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import com.raymond.bookingsystem.error.*;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final BookingRepository bookingRepository;

    public CustomerService(CustomerRepository customerRepository, BookingRepository bookingRepository) {
        this.customerRepository = customerRepository;
        this.bookingRepository = bookingRepository;
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }


    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Customer not found: " + id));
    }

    //UPPDATERA KUND
    public Customer updateCustomer(Long id, Customer updatedCustomer) {

        Customer existingCustomer = customerRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException("Customer not found: " + id));

        //Nya information får bytas ut
        if (updatedCustomer.getName() != null) {
            existingCustomer.setName(updatedCustomer.getName());
        }

        if (updatedCustomer.getEmail() != null) {
            existingCustomer.setEmail(updatedCustomer.getEmail());
        }

        if (updatedCustomer.getPhoneNumber() != null) {
            existingCustomer.setPhoneNumber(updatedCustomer.getPhoneNumber());
        }

        return customerRepository.save(existingCustomer);
    }


    //DELETE
    public void deleteCustomer(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Customer not found: " + id));

        boolean hasActiveBookings = customer.getBookings()
                .stream()
                .anyMatch(b -> b.getStatus() == BookingStatus.ACTIVE);

        if (hasActiveBookings) {
            throw new BadRequestException(
                    "Du har aktiva bokningar. Avboka eller radera dem innan du tar bort kontot."
            );
        }

        customerRepository.delete(customer);
    }

    public Customer createCustomer(CreateCustomerRequest request) {
        Customer customer = new Customer();
        customer.setName(request.name());
        customer.setEmail(request.email());
        customer.setPhoneNumber(request.phoneNumber());
        customer.setPassword(request.password());
        customer.setRole("Customer");

        return customerRepository.save(customer);
    }
}
