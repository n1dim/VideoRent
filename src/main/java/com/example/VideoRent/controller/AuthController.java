package com.example.VideoRent.controller;
import com.example.VideoRent.service.UserService;
import com.example.VideoRent.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String form() {
        return "register";
    }

    @PostMapping("/register")
    public String register(User user) {
        userService.register(user);
        return "redirect:/register?success";
    }
}