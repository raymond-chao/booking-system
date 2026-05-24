package com.raymond.bookingsystem.controllers;

import com.raymond.bookingsystem.model.Room;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.raymond.bookingsystem.service.RoomService;


import java.util.List;

@RestController
@RequestMapping("/api/rooms")
@CrossOrigin(origins = "*")
public class RoomController {


    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }


    @GetMapping
    public List<Room> getAllRooms() {
        return roomService.getAllRooms();
    }
}
