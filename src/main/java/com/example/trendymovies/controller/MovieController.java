package com.example.trendymovies.controller;

import com.example.trendymovies.model.Movie;
import com.example.trendymovies.service.MovieService;
import com.example.trendymovies.service.TmdbApiService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/movies")
public class MovieController {
    private final TmdbApiService tmdbApiService;
    private final MovieService movieService;

    public MovieController(TmdbApiService tmdbApiService, MovieService movieService) {
        this.tmdbApiService = tmdbApiService;
        this.movieService = movieService;
    }

    @GetMapping
    public String getTrendingMovies(Model model) {
        List<Movie> trendingMovies = tmdbApiService.getTrendingMovies();
        // Update each movie with favorite status from database
        trendingMovies.forEach(movie -> {
            Movie savedMovie = movieService.getMovieByTmdbId(movie.getTmdbId());
            if (savedMovie != null) {
                movie.setIsFavorite(savedMovie.getIsFavorite());
            }
        });
        model.addAttribute("movies", trendingMovies);
        model.addAttribute("pageTitle", "Trending Movies");
        return "movie-list";
    }

    @GetMapping("/search")
    public String searchMovies(@RequestParam String query, Model model) {
        List<Movie> searchResults = tmdbApiService.searchMovies(query);
        // Update each movie with favorite status from database
        searchResults.forEach(movie -> {
            Movie savedMovie = movieService.getMovieByTmdbId(movie.getTmdbId());
            if (savedMovie != null) {
                movie.setIsFavorite(savedMovie.getIsFavorite());
            }
        });
        model.addAttribute("movies", searchResults);
        model.addAttribute("pageTitle", "Search Results for: " + query);
        return "movie-list";
    }

    @GetMapping("/{tmdbId}")
    public String getMovieDetails(@PathVariable Integer tmdbId, Model model) {
        Movie movie = tmdbApiService.getMovieDetails(tmdbId);
        Movie savedMovie = movieService.getMovieByTmdbId(tmdbId);

        if (savedMovie != null) {
            movie.setIsFavorite(savedMovie.isFavorite()); // Using isFavorite() instead of getIsFavorite()
        }

        model.addAttribute("movie", movie);
        return "movie-details";
    }

    @PostMapping("/toggle-favorite")
    public String toggleFavorite(@ModelAttribute Movie movie) {
        movieService.toggleFavoriteStatus(movie);
        return "redirect:/movies/" + movie.getTmdbId();
    }

    @GetMapping("/favorites")
    public String getFavoriteMovies(Model model) {
        model.addAttribute("movies", movieService.getAllFavoriteMovies());
        model.addAttribute("pageTitle", "Favorite Movies");
        return "movie-list";
    }
}