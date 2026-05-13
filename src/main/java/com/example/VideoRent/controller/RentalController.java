package com.example.VideoRent.controller;

import com.example.VideoRent.service.IssueResult;
import com.example.VideoRent.service.RentalService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Controller
public class RentalController {

    private final RentalService rentalService;

    public RentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    @PostMapping("/rentals/{id}/return")
    public String returnRental(@PathVariable Long id, @RequestParam Long userId) {
        rentalService.returnRental(id);
        return "redirect:/admin/users/" + userId;
    }

    @PostMapping("/movie/{movieId}/issue")
    public String issue(@PathVariable Long movieId,
                        @RequestParam Long copyId,
                        @RequestParam String phone,
                        @RequestParam String dueDate) {
        LocalDate due;
        try {
            due = LocalDate.parse(dueDate);
        } catch (Exception e) {
            due = null;
        }
        IssueResult result = rentalService.issue(copyId, phone, due);
        if (result != IssueResult.SUCCESS) {
            return "redirect:/movie/" + movieId + "?issueError=" + result.name();
        }
        return "redirect:/movie/" + movieId;
    }
}
