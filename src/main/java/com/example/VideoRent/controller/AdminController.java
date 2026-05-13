package com.example.VideoRent.controller;

import com.example.VideoRent.service.MovieService;
import com.example.VideoRent.service.RentalService;
import com.example.VideoRent.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Controller
public class AdminController {

    private final UserService userService;
    private final MovieService movieService;
    private final RentalService rentalService;

    public AdminController(UserService userService, MovieService movieService, RentalService rentalService) {
        this.userService = userService;
        this.movieService = movieService;
        this.rentalService = rentalService;
    }

    @GetMapping("/admin")
    public String adminPanel(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("userCount", userService.getAllUsers().size());
        model.addAttribute("movieCount", movieService.getAllMovies().size());
        model.addAttribute("activeRentals", rentalService.countActiveRentals());
        return "admin";
    }

    @GetMapping("/admin/users/{id}")
    public String userPage(@PathVariable Long id, Model model) {
        model.addAttribute("user", userService.getUserById(id));
        model.addAttribute("rentals", rentalService.getRentalsByUser(id));
        model.addAttribute("today", LocalDate.now());
        return "admin-user";
    }

    @PostMapping("/movie/{movieId}/return")
    public String returnFilm(@PathVariable Long movieId, @RequestParam String phone) {
        boolean ok = rentalService.returnByMovieAndPhone(movieId, phone);
        return ok ? "redirect:/movie/" + movieId
                  : "redirect:/movie/" + movieId + "?returnError";
    }

    @PostMapping("/admin/users/{id}/block")
    public String blockUser(@PathVariable Long id, @RequestParam String reason) {
        userService.blockUser(id, reason);
        return "redirect:/admin/users/" + id;
    }

    @PostMapping("/admin/users/{id}/unblock")
    public String unblockUser(@PathVariable Long id) {
        userService.unblockUser(id);
        return "redirect:/admin/users/" + id;
    }

    @PostMapping("/rentals/{id}/approve-extension")
    public String approveExtension(@PathVariable Long id, @RequestParam Long userId) {
        rentalService.approveExtension(id);
        return "redirect:/admin/users/" + userId;
    }

    @PostMapping("/rentals/{id}/reject-extension")
    public String rejectExtension(@PathVariable Long id, @RequestParam Long userId) {
        rentalService.rejectExtension(id);
        return "redirect:/admin/users/" + userId;
    }
}
