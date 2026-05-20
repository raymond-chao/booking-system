package com.raymond.bookingsystem.service;

import com.raymond.bookingsystem.error.NotFoundException;
import com.raymond.bookingsystem.model.Room;
import com.raymond.bookingsystem.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {

    private final RoomRepository repository;

    public RoomService(RoomRepository repository) {
        this.repository = repository;
    }

    public List<Room> getAllRooms() {
        return repository.findAll();
    }


    public Room getRoomById(Long id) {
        return repository.findById(id)
                .orElseThrow(()  -> new NotFoundException("Rummet hittades inte"));
    }



}