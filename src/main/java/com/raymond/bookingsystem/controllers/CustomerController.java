package com.raymond.bookingsystem.controllers;

import com.raymond.bookingsystem.model.CreateCustomerRequest;
import com.raymond.bookingsystem.model.Customer;
import com.raymond.bookingsystem.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/customers")
@CrossOrigin(origins = "*")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    // CREATE CUSTOMER
    @PostMapping
    public Customer createCustomer(@RequestBody CreateCustomerRequest request) {
        return customerService.createCustomer(request);
    }

    // GET ALL CUSTOMERS
    @GetMapping
    public List<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @GetMapping("/{id}")
    public Customer getCustomerById(@PathVariable Long id) {
        return customerService.getCustomerById(id);
    }

    // UPDATE CUSTOMER
    @PutMapping("/{id}")
    public Customer updateCustomer(@PathVariable Long id,
                                   @RequestBody Customer updatedCustomer) {

        return customerService.updateCustomer(id, updatedCustomer);
    }



    //DELETE CUSTOMER
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }


}
