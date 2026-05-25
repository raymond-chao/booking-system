package com.raymond.bookingsystem.controllers;

import com.raymond.bookingsystem.service.RoomService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RoomWebController {

    private final RoomService roomService;

    public RoomWebController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping("/rooms")
    public String showAllRooms(Model model) {
        model.addAttribute("rooms", roomService.getAllRooms());
        model.addAttribute("pageTitle", "Alla Rum - Pensionat RMJ");
        return "rooms";
    }

    @GetMapping("/available-rooms")
    public String showAvailableRooms(Model model) {
        model.addAttribute("rooms", roomService.getAvailableRooms());
        model.addAttribute("pageTitle", "Lediga Rum - Pensionat RMJ");
        return "available-rooms";
    }
}