package com.raymond.bookingsystem.controllers;


import com.raymond.bookingsystem.error.BadRequestException;
import com.raymond.bookingsystem.model.CreateCustomerRequest;
import com.raymond.bookingsystem.model.Customer;
import org.springframework.ui.Model;
import com.raymond.bookingsystem.service.CustomerService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/customers")
public class CustomerWebController {

    private final CustomerService customerService;

    public CustomerWebController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public String showCustomers(Model model) {
        model.addAttribute("customers", customerService.getAllCustomers());
        return "customers"; // HTML-fil
    }

    @GetMapping("/new")
    public String showForm(Model model) {
        model.addAttribute("customer", new Customer());
        return "customer-form";
    }



    @PostMapping("/save")
    public String saveCustomer(@ModelAttribute Customer customer,
                               RedirectAttributes redirectAttributes) {

        if (customer.getId() != null) {
            customerService.updateCustomer(customer.getId(), customer);
        } else {
            CreateCustomerRequest request = new CreateCustomerRequest(
                    customer.getName(),
                    customer.getEmail(),
                    customer.getPhoneNumber(),
                    customer.getPassword()
            );

            customerService.createCustomer(request);
            redirectAttributes.addFlashAttribute("Success", "Välkommen! Dit konto är skapad");
        }

        return "redirect:/rooms";
    }



    @PostMapping("/delete/{id}")
    public String deleteCustomer(@PathVariable Long id, RedirectAttributes redirectAttributes) {

        try {
            customerService.deleteCustomer(id);
            redirectAttributes.addFlashAttribute("success", "Kund raderad");
        } catch (BadRequestException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/customers";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Customer customer = customerService.findById(id);
        model.addAttribute("customer", customer);
        return "customer-form";
    }

    @PostMapping("/ui")
    public String createCustomerUI(@ModelAttribute CreateCustomerRequest request) {
        customerService.createCustomer(request);
        return "redirect:/customers";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register"; // register.html
    }

    @PostMapping("/account/update")
    public String updateAccount(@ModelAttribute Customer customer) {

        customerService.updateCustomer(customer.getId(), customer);

        return "redirect:/account";
    }



}
