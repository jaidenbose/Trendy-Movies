package com.example.trendymovies.service;

import com.example.trendymovies.dto.TmdbMovie;
import com.example.trendymovies.dto.TmdbMovieResponse;
import com.example.trendymovies.model.Movie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TmdbApiService {
    private final RestTemplate restTemplate;

    @Value("${tmdb.api.key}")
    private String apiKey;

    @Value("${tmdb.api.base-url}")
    private String baseUrl;

    public TmdbApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<Movie> getTrendingMovies() {
        String url = UriComponentsBuilder.fromUriString(baseUrl + "/trending/movie/week")
                .queryParam("api_key", apiKey)
                .build()
                .toUriString();

        TmdbMovieResponse response = restTemplate.getForObject(url, TmdbMovieResponse.class);

        return response.getResults().stream()
                .map(this::convertToMovie)
                .collect(Collectors.toList());
    }

    public Movie getMovieDetails(Integer movieId) {
        String url = UriComponentsBuilder.fromUriString(baseUrl + "/movie/" + movieId)
                .queryParam("api_key", apiKey)
                .build()
                .toUriString();

        TmdbMovie tmdbMovie = restTemplate.getForObject(url, TmdbMovie.class);

        return convertToMovie(tmdbMovie);
    }

    public List<Movie> searchMovies(String query) {
        String url = UriComponentsBuilder.fromUriString(baseUrl + "/search/movie")
                .queryParam("api_key", apiKey)
                .queryParam("query", query)
                .build()
                .toUriString();

        TmdbMovieResponse response = restTemplate.getForObject(url, TmdbMovieResponse.class);

        return response.getResults().stream()
                .map(this::convertToMovie)
                .collect(Collectors.toList());
    }

    private Movie convertToMovie(TmdbMovie tmdbMovie) {
        if (tmdbMovie == null) {
            return null;
        }

        return new Movie(
                tmdbMovie.getId(),
                tmdbMovie.getTitle(),
                tmdbMovie.getOverview(),
                tmdbMovie.getPoster_path(),
                tmdbMovie.getRelease_date(),
                tmdbMovie.getVote_average()
        );
    }
}