package com.raymond.bookingsystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // disable CSRF (ok för projekt, men i riktig app bör du ha det på)
                .csrf(csrf -> csrf.disable())

                // regler för vilka sidor som är öppna / skyddade
                .authorizeHttpRequests(auth -> auth

                        // PUBLIC PAGES (alla kan se)
                        .requestMatchers(
                                "/",
                                "/login",
                                "/customers/new",
                                "/available-rooms",
                                "/rooms",
                                "/book-room/check-availability",
                                "/css/**",
                                "/js/**"
                        ).permitAll()

                        // BOOKING KRÄVER LOGIN
                        .requestMatchers(
                                "/book-room",
                                "/account/**",
                                "/my-bookings/**"
                        ).authenticated()

                        // allt annat är fritt (eller kan du ändra senare)
                        .anyRequest().permitAll()
                )

                // LOGIN SETUP
                .formLogin(form -> form
                        .loginPage("/customers/new")
                        .loginProcessingUrl("/login")
                        .usernameParameter("username")  // email
                        .passwordParameter("password")
                        .defaultSuccessUrl("/account", true)
                        .defaultSuccessUrl("/rooms", true)
                        .permitAll()
                )

                // LOGOUT
                .logout(logout -> logout
                        .logoutSuccessUrl("/")


                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}