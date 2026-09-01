package com.example.customer_service.service;

import com.example.customer_service.model.CreateCustomerRequest;
import com.example.customer_service.model.Customer;
import com.example.customer_service.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    public CustomerService(CustomerRepository customerRepository,
                           PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    //Hämta alla kunder
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    //Hämta specifik kund med ID
    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found: "+ id));
    }

    //Skapa customer
    public Customer createCustomer(CreateCustomerRequest request) {
        Customer customer = new Customer();

        customer.setName(request.name());
        customer.setEmail(request.email());
        customer.setPhoneNumber(request.phoneNumber());

        customer.setPassword(
                passwordEncoder.encode(request.password())
        );

        return customerRepository.save(customer);
    }

    public Customer updateCustomer(Long id, Customer updatedCustomer) {

        Customer existingCustomer = customerRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Customer not found: " + id));

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

    public void deleteCustomer(Long id) {

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Customer not found: " + id));

        customerRepository.delete(customer);
    }
}