package com.raymond.bookingsystem.service;

import com.raymond.bookingsystem.model.Booking;
import com.raymond.bookingsystem.model.CreateCustomerRequest;
import com.raymond.bookingsystem.repository.*;
import com.raymond.bookingsystem.model.Customer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

import com.raymond.bookingsystem.error.*;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final BookingRepository bookingRepository;


    private PasswordEncoder passwordEncoder;

//    public CustomerService(CustomerRepository customerRepository, BookingRepository bookingRepository) {
//        this.customerRepository = customerRepository;
//        this.bookingRepository = bookingRepository;
//    }

public CustomerService(CustomerRepository customerRepository,
                       BookingRepository bookingRepository,
                       PasswordEncoder passwordEncoder) {
    this.customerRepository = customerRepository;
    this.bookingRepository = bookingRepository;
    this.passwordEncoder = passwordEncoder;
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

        List<Booking> bookings = bookingRepository.findAllByCustomerId(id);

        boolean hasActiveBookings = bookings.stream()
                .anyMatch(b -> b.getStatus() == BookingStatus.ACTIVE);

        if (hasActiveBookings) {
            throw new BadRequestException(
                    "Du har aktiva bokningar. Avboka eller radera dem innan du tar bort kontot."
            );
        }

        customerRepository.delete(customer);
    }


    public boolean hasActiveBookings(Long customerId) {
        return bookingRepository.existsByCustomerId(customerId);
    }


//    public Customer save(Customer customer) {
//        if (customerRepository.existsByEmail(customer.getEmail())) {
//           throw new BadRequestException("Email already exists");
//        }
//        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
//        return customerRepository.save(customer);
//    }


    public Optional<Customer> findByEmail(String email) {
        return customerRepository.findByEmail(email);
    }


    public Customer findById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
    }

    public Customer createCustomer(CreateCustomerRequest request) {
        Customer customer = new Customer();
        customer.setName(request.name());
        customer.setEmail(request.email());
        customer.setPhoneNumber(request.phoneNumber());
        customer.setPassword(passwordEncoder.encode(request.password()));

        return customerRepository.save(customer);
    }
}
