package com.raymond.bookingsystem.controllers;
import com.raymond.bookingsystem.model.Customer;
import com.raymond.bookingsystem.service.CustomerService;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class AccountController {

    private final CustomerService customerService;

    public AccountController(CustomerService customerService) {
        this.customerService = customerService;
    }

//    @GetMapping("/account")
//    public String accountPage(Model model,
//                              Authentication authentication) {
//
//        String email = authentication.getName();
//        model.addAttribute("email", email);
//
//        return "account";
//    }


    @GetMapping("/account")
    public String accountPage(Model model, Authentication authentication) {

        String email = authentication.getName();

        Customer customer = customerService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        model.addAttribute("customer", customer);
        model.addAttribute("bookings", customer.getBookings());

        return "account";
    }


    @GetMapping("/account/edit")
    public String editAccountPage(Model model,
                                  Authentication authentication) {

        String email = authentication.getName();

        Customer customer = customerService
                .findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        model.addAttribute("customer", customer);

        return "edit-account";
    }

    @PostMapping("/account/update")
    public String updateAccount(Customer customer,
                                Authentication authentication) {

        String email = authentication.getName();

        Customer existing = customerService
                .findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        // uppdatera fält (behåller ID)
        existing.setName(customer.getName());
        existing.setEmail(customer.getEmail());
        existing.setPhoneNumber(customer.getPhoneNumber());

        customerService.updateCustomer(existing.getId(), existing);

        return "redirect:/account";
    }


    @PostMapping("/account/delete")
    public String deleteAccount(Authentication authentication,
                                RedirectAttributes redirectAttributes,
                                HttpServletRequest request,
                                HttpServletResponse response) {

        String email = authentication.getName();

        Customer customer = customerService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        if (customerService.hasActiveBookings(customer.getId())) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "Du kan inte radera kontot eftersom du har aktiva bokningar."
            );

            return "redirect:/account/edit";
        }

        customerService.deleteCustomer(customer.getId());

        new SecurityContextLogoutHandler().logout(request, response, authentication);

        redirectAttributes.addFlashAttribute(
                "successMessage",
                "Ditt konto har raderats. Du är nu utloggad."
        );

        return "index";
    }

}