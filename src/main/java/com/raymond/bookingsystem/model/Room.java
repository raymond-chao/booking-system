package com.raymond.bookingsystem.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "rooms")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Rumsnummer måste anges")
    @Column(nullable = false, unique = true, length = 10)
    private String roomNumber;

    @Min(value = 1, message = "Ett rum måste ha minst en säng")
    @Column(nullable = false)
    private int beds;

    @NotNull(message = "Pris måste anges")
    @DecimalMin(value = "0.01", message = "Pris per natt måste vara större än 0")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal pricePerNight;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private boolean available = true;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Booking> bookings = new ArrayList<>();
    
    public Room(String roomNumber, int beds, BigDecimal pricePerNight, String description) {
        this.roomNumber = roomNumber;
        this.beds = beds;
        this.pricePerNight = pricePerNight;
        this.description = description;
        this.available = true;
    }
}