package com.example.VideoRent.controller;
import com.example.VideoRent.service.UserService;
import com.example.VideoRent.service.RegisterResult;
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

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    @GetMapping("/register")
    public String registerForm() {
        return "register";
    }

    @PostMapping("/register")
    public String register(User user) {
        RegisterResult result = userService.register(user);
        return switch (result) {
            case SUCCESS -> "redirect:/register?success";
            case PHONE_TAKEN -> "redirect:/register?phoneTaken";
            case INVALID_PHONE -> "redirect:/register?invalidPhone";
            case MISSING_FIELDS -> "redirect:/register?missingFields";
        };
    }
}