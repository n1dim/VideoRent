package com.example.VideoRent.controller;

import com.example.VideoRent.entity.User;
import com.example.VideoRent.service.RentalService;
import com.example.VideoRent.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Controller
public class ProfileController {

    private final UserService userService;
    private final RentalService rentalService;

    public ProfileController(UserService userService, RentalService rentalService) {
        this.userService = userService;
        this.rentalService = rentalService;
    }

    @GetMapping("/profile")
    public String profile(HttpSession session, Model model) {
        User user = (User) session.getAttribute("currentUser");
        if (user == null) return "redirect:/login";
        model.addAttribute("user", user);
        model.addAttribute("rentals", rentalService.getRentalsByUser(user.getId()));
        model.addAttribute("today", LocalDate.now());
        return "profile";
    }

    @PostMapping("/profile/edit")
    public String editProfile(HttpSession session,
                              @RequestParam String fullName,
                              @RequestParam String homeAddress) {
        User user = (User) session.getAttribute("currentUser");
        if (user == null) return "redirect:/login";
        userService.updateProfile(user.getId(), fullName, homeAddress);
        session.setAttribute("currentUser", userService.getUserById(user.getId()));
        return "redirect:/profile";
    }

    @PostMapping("/profile/password")
    public String changePassword(HttpSession session,
                                 @RequestParam String oldPassword,
                                 @RequestParam String newPassword) {
        User user = (User) session.getAttribute("currentUser");
        if (user == null) return "redirect:/login";
        boolean ok = userService.changePassword(user.getId(), oldPassword, newPassword);
        return ok ? "redirect:/profile" : "redirect:/profile?passwordError";
    }

    @PostMapping("/rentals/{id}/extend")
    public String requestExtension(@PathVariable Long id,
                                   @RequestParam String requestedDate,
                                   HttpSession session) {
        User user = (User) session.getAttribute("currentUser");
        if (user == null) return "redirect:/login";
        try {
            rentalService.requestExtension(id, LocalDate.parse(requestedDate));
        } catch (Exception e) {
            return "redirect:/profile";
        }
        return "redirect:/profile";
    }
}
