package com.example.VideoRent.controller;

import com.example.VideoRent.service.MediaService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MediaController {

    private final MediaService mediaService;

    public MediaController(MediaService mediaService) {
        this.mediaService = mediaService;
    }

    @PostMapping("/media/add")
    public String addMedia(@RequestParam String name,
                           @RequestParam String returnTo) {
        if (name != null && !name.isBlank()) {
            mediaService.save(name);
        }
        return "redirect:" + returnTo;
    }
}
