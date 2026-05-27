//package com.raymond.bookingsystem.controllers;
//
//import com.raymond.bookingsystem.model.Room;
//import com.raymond.bookingsystem.service.RoomService;
//import org.springframework.format.annotation.DateTimeFormat;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.time.LocalDate;
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/rooms")
//@CrossOrigin(origins = "http://localhost:8080")
//public class RoomController {
//
//    private final RoomService roomService;
//
//    public RoomController(RoomService roomService) {
//        this.roomService = roomService;
//    }
//
//    @GetMapping
//    public List<Room> getAllRooms() {
//        return roomService.getAllRooms();
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<Room> getRoomById(@PathVariable Long id) {
//        return ResponseEntity.ok(roomService.getRoomById(id));
//    }
//
//    @GetMapping("/available")
//    public List<Room> getAvailableRooms(
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkIn,
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOut
//    ) {
//        return roomService.getAvailableRooms(checkIn, checkOut);
//    }
//
//    @GetMapping("/beds/{beds}")
//    public List<Room> getRoomsByBeds(@PathVariable int beds) {
//        return roomService.getRoomsByBeds(beds);
//    }
//}