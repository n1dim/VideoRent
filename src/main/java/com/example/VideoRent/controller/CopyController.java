package com.example.VideoRent.controller;

import com.example.VideoRent.dao.MediaDao;
import com.example.VideoRent.dao.MovieDao;
import com.example.VideoRent.entity.Copy;
import com.example.VideoRent.service.CopyService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CopyController {

    private final CopyService copyService;
    private final MovieDao movieDao;
    private final MediaDao mediaDao;

    public CopyController(CopyService copyService, MovieDao movieDao, MediaDao mediaDao) {
        this.copyService = copyService;
        this.movieDao = movieDao;
        this.mediaDao = mediaDao;
    }

    @GetMapping("/movie/{movieId}/copies/add")
    public String addCopyForm(@PathVariable Long movieId, Model model) {
        model.addAttribute("movie", movieDao.getById(movieId));
        model.addAttribute("mediaList", mediaDao.getAll());
        model.addAttribute("editMode", false);
        model.addAttribute("formAction", "/movie/" + movieId + "/copies/add");
        return "copy-form";
    }

    @PostMapping("/movie/{movieId}/copies/add")
    public String addCopy(@PathVariable Long movieId, Copy copy,
                          @RequestParam Long mediaId) {
        copy.setFilm(movieDao.getById(movieId));
        copy.setMedia(mediaDao.getById(mediaId));
        copyService.save(copy);
        return "redirect:/movie/" + movieId;
    }

    @GetMapping("/movie/{movieId}/copies/edit/{copyId}")
    public String editCopyForm(@PathVariable Long movieId,
                               @PathVariable Long copyId, Model model) {
        model.addAttribute("movie", movieDao.getById(movieId));
        model.addAttribute("copy", copyService.getCopyById(copyId));
        model.addAttribute("editMode", true);
        model.addAttribute("formAction", "/movie/" + movieId + "/copies/edit/" + copyId);
        return "copy-form";
    }

    @PostMapping("/movie/{movieId}/copies/edit/{copyId}")
    public String editCopy(@PathVariable Long movieId,
                           @PathVariable Long copyId,
                           Copy updated) {
        copyService.updateCopy(copyId, updated);
        return "redirect:/movie/" + movieId;
    }
}
