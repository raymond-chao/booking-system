package com.raymond.bookingsystem.service;

import com.raymond.bookingsystem.repository.BookingRepository;
import com.raymond.bookingsystem.model.CreateCustomerRequest;
import com.raymond.bookingsystem.repository.CustomerRepository;
import com.raymond.bookingsystem.model.Customer;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public Customer createCustomer(CreateCustomerRequest request) {
        Customer customer = new Customer(request.name(), request.email(), request.phone());
        return customerRepository.save(customer);
    }
}
