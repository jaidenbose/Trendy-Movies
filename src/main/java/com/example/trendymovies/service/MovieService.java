package com.example.trendymovies.service;

import com.example.trendymovies.model.Movie;
import com.example.trendymovies.repository.MovieRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieService {
    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public List<Movie> getAllFavoriteMovies() {
        return movieRepository.findByIsFavoriteTrue();
    }

    public Movie toggleFavoriteStatus(Movie movie) {
        Movie existingMovie = movieRepository.findByTmdbId(movie.getTmdbId());

        if (existingMovie != null) {
            existingMovie.setIsFavorite(!existingMovie.getIsFavorite());
            return movieRepository.save(existingMovie);
        } else {
            movie.setIsFavorite(true);
            return movieRepository.save(movie);
        }
    }

    public Movie getMovieByTmdbId(Integer tmdbId) {
        return movieRepository.findByTmdbId(tmdbId);
    }
}