package com.example.VideoRent.controller;

import com.example.VideoRent.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import com.example.VideoRent.entity.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public String login(String telephoneNumber, String password,
                        HttpSession session, RedirectAttributes redirectAttributes) {
        User user = userService.login(telephoneNumber, password);
        if (user == null) return "redirect:/login?error";
        if (Boolean.TRUE.equals(user.getIsBlocked())) {
            redirectAttributes.addFlashAttribute("blockedReason", user.getBlockReason());
            return "redirect:/login";
        }
        session.setAttribute("currentUser", user);
        return "redirect:/";
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }


}