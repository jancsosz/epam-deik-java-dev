package com.epam.training.ticketservice.service;

import com.epam.training.ticketservice.model.Movie;
import com.epam.training.ticketservice.repository.MovieRepository;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieService {

    private static final String MOVIE_NOT_FOUND = "Movie with given title not found.";
    private static final String MOVIE_ALREADY_EXIST = "Movie with given title already exists";

    private final MovieRepository movieRepository;


    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    public Movie findByTitle(String title) throws NotFoundException {

        Movie movie = movieRepository.findByTitle(title);

        if (movie != null) {
            return movieRepository.findByTitle(title);
        } else {
            throw new NotFoundException(MOVIE_NOT_FOUND);
        }
    }

    public Movie mapToMovie(String title, String genre, int runningTime) {
        return Movie.builder()
                .title(title)
                .genre(genre)
                .runningTime(runningTime)
                .build();
    }

    public void createMovie(Movie movie) throws Exception {
        if (!movieRepository.existsByTitle(movie.getTitle())) {
            movieRepository.save(movie);
        } else {
            throw new Exception(MOVIE_ALREADY_EXIST);
        }
    }

    public void updateMovie(Movie movie) throws NotFoundException {
        if (movieRepository.existsByTitle(movie.getTitle())) {
            movieRepository.update(movie.getTitle(), movie.getGenre(), movie.getRunningTime());
        } else {
            throw new NotFoundException(MOVIE_NOT_FOUND);
        }
    }

    public void deleteMovie(String title) throws NotFoundException {
        if (movieRepository.existsByTitle(title)) {
            movieRepository.deleteByTitle(title);
        } else {
            throw new NotFoundException(MOVIE_NOT_FOUND);
        }
    }


}
