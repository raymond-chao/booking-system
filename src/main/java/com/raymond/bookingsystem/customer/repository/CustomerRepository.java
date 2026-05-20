package com.raymond.bookingsystem.customer.repository;

import com.raymond.bookingsystem.customer.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
