package com.raymond.bookingsystem.controllers;

import com.raymond.bookingsystem.client.CustomerClient;
import com.raymond.bookingsystem.DTO.CreateCustomerRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


//Det sparas ingen kund i Booking service, Controll tar bara emot formuläret och skickar uppgifter vidare till Customer service via REST.

@Controller
public class CustomerWebController {

    private final CustomerClient customerClient;

    public CustomerWebController(CustomerClient customerClient) {
        this.customerClient = customerClient;
    }

    @PostMapping("/customers/save")
    public String createCustomer(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String phoneNumber,
            @RequestParam String password) {

        CreateCustomerRequest request =
                new CreateCustomerRequest(name, email, phoneNumber, password);

        customerClient.createCustomer(request);

        return "redirect:/customers/new";
    }


    @GetMapping("/customers/new")
    public String showCustomerForm(Model model) {
        model.addAttribute(
                "customer",
                new CreateCustomerRequest("", "", "", "")
        );

        return "customer-form";
    }


}
