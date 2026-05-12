package com.example.VideoRent.controller;

import com.example.VideoRent.service.MovieService;
import com.example.VideoRent.service.CopyService;
import com.example.VideoRent.entity.Movie;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MovieController {

    private final MovieService movieService;
    private final CopyService copyService;

    public MovieController(MovieService movieService, CopyService copyService) {
        this.movieService = movieService;
        this.copyService = copyService;
    }

    @GetMapping("/")
    public String index(Model model, @RequestParam(required = false) String search) {
        model.addAttribute("movies", movieService.search(search));
        model.addAttribute("search", search);
        return "movies";
    }

    @GetMapping("/movie/{movieId}")
    public String moviePage(@PathVariable Long movieId, Model model) {
        model.addAttribute("movie", movieService.getMovie(movieId));
        model.addAttribute("media", copyService.getCopiesByMovie(movieId));
        return "movie";
    }

    @GetMapping("/movies/add")
    public String addMovieForm() {
        return "movie-form";
    }

    @PostMapping("/movies/add")
    public String addMovie(Movie movie) {
        movieService.addMovie(movie);
        return "redirect:/";
    }

    @GetMapping("/movies/edit/{id}")
    public String editMovieForm(@PathVariable Long id, Model model) {
        model.addAttribute("movie", movieService.getMovie(id));
        model.addAttribute("editMode", true);
        return "movie-form";
    }

    @PostMapping("/movies/edit/{id}")
    public String editMovie(@PathVariable Long id, Movie movie) {
        movieService.updateMovie(id, movie);
        return "redirect:/movie/" + id;
    }

    @PostMapping("/movies/delete/{id}")
    public String deleteMovie(@PathVariable Long id) {
        movieService.deleteMovie(id);
        return "redirect:/";
    }

}
