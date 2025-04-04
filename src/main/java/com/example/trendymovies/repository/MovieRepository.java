package com.example.trendymovies.repository;

import com.example.trendymovies.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    List<Movie> findByIsFavoriteTrue();
    Movie findByTmdbId(Integer tmdbId);
}