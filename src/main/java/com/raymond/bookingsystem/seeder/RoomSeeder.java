package com.raymond.bookingsystem.seeder;

import com.raymond.bookingsystem.model.Room;
import com.raymond.bookingsystem.repository.RoomRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class RoomSeeder implements CommandLineRunner {

    private final RoomRepository roomRepository;

    public RoomSeeder(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Override
    public void run(String... args) {
        if (roomRepository.count() == 0) {
            // Enkelrum
            roomRepository.save(new Room("101", 1, new BigDecimal("800.00"), "Mysigt enkelrum med utsikt mot gården"));
            roomRepository.save(new Room("102", 1, new BigDecimal("800.00"), "Enkelt och funktionellt rum"));
            roomRepository.save(new Room("103", 1, new BigDecimal("850.00"), "Enkelrum med balkong"));
            roomRepository.save(new Room("104", 1, new BigDecimal("800.00"), "Kompakt rum perfekt för en person"));

            // Dubbelrum
            roomRepository.save(new Room("105", 2, new BigDecimal("1100.00"), "Rymligt dubbelrum med två separata sängar"));
            roomRepository.save(new Room("106", 2, new BigDecimal("1100.00"), "Dubbelrum med dubbelsäng"));
            roomRepository.save(new Room("107", 2, new BigDecimal("1200.00"), "Dubbelrum med havsutsikt"));

            // Familjerum
            roomRepository.save(new Room("108", 3, new BigDecimal("2100.00"), "Familjerum med plats för 3 personer"));
            roomRepository.save(new Room("109", 3, new BigDecimal("2100.00"), "Stort familjerum med extra säng"));
            roomRepository.save(new Room("110", 3, new BigDecimal("2300.00"), "Deluxe familjerum med balkong"));

            System.out.println(" 10 rum har skapats i databasen");
        }
    }
}