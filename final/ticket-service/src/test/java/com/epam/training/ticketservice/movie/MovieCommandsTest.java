package com.epam.training.ticketservice.movie;

import com.epam.training.ticketservice.commands.MovieCommands;
import com.epam.training.ticketservice.exception.CustomException;
import com.epam.training.ticketservice.model.Movie;
import com.epam.training.ticketservice.repository.MovieRepository;
import com.epam.training.ticketservice.service.MovieService;
import javassist.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MovieCommandsTest {

    private Movie movie;

    @Mock
    MovieService movieService;

    @Mock
    MovieRepository movieRepository;

    @InjectMocks
    MovieCommands movieCommands;


    @BeforeEach
    private void setUp() {
        movie = Movie.builder()
                .title("movie")
                .genre("asd")
                .runningTime(120)
                .build();
    }

    @Test
    public void testCreateMovieShouldPersistWhenMovieIsOk() throws CustomException {
        // Given
        // When
        when(movieService.mapToMovie(movie.getTitle(), movie.getGenre(), movie.getRunningTime())).thenReturn(movie);
        movieCommands.createMovie(movie.getTitle(), movie.getGenre(), movie.getRunningTime());

        // Then
        verify(movieService, times(1)).createMovie(any(Movie.class));
    }

    @Test
    public void testCreateMovieShouldThrowCustomExceptionWhenMovieAlreadyExists() throws CustomException {
        // Given
        // When
        when(movieService.mapToMovie(movie.getTitle(), movie.getGenre(), movie.getRunningTime())).thenReturn(movie);
        doThrow(CustomException.class).when(movieService).createMovie(any(Movie.class));
        movieCommands.createMovie(movie.getTitle(), movie.getGenre(), movie.getRunningTime());

        // Then
        verify(movieRepository, times(0)).save(any(Movie.class));
    }

    @Test
    public void testUpdateMovieShouldPersistWhenMovieIsOk() throws NotFoundException {
        // Given
        // When
        when(movieService.mapToMovie(movie.getTitle(), movie.getGenre(), movie.getRunningTime())).thenReturn(movie);
        movieCommands.updateMovie(movie.getTitle(), movie.getGenre(), movie.getRunningTime());

        // Then
        verify(movieService, times(1)).updateMovie(any(Movie.class));
    }

    @Test
    public void testUpdateMovieShouldNotUpdateMovieWhenMovieIsNotFound() throws NotFoundException {
        // Given
        // When
        when(movieService.mapToMovie(movie.getTitle(), movie.getGenre(), movie.getRunningTime())).thenReturn(movie);
        doThrow(NotFoundException.class).when(movieService).updateMovie(any(Movie.class));
        movieCommands.updateMovie(movie.getTitle(), movie.getGenre(), movie.getRunningTime());

        // Then
        verify(movieRepository, times(0)).update("asd", "asd", 1);
    }

    @Test
    public void testDeleteMovieShouldDeleteMovieWhenMovieIsOk() throws NotFoundException {

        // Given
        String title = "movie";

        // When
        movieCommands.deleteMovie(title);

        // Then
        verify(movieService, times(1)).deleteMovie(title);
    }

    @Test
    public void testDeleteMovieShouldNotDeleteMovieWhenNotFoundException()
            throws NotFoundException {

        // Given
        String title = "movie";

        // When
        doThrow(NotFoundException.class).when(movieService).deleteMovie(title);
        movieCommands.deleteMovie(title);

        // Then
        verify(movieRepository, times(0)).deleteByTitle(title);
    }

    @Test
    public void testListMoviesShouldReturnWithFoundMovies() {

        // Given
        Movie movie1 = Movie.builder()
                .title("test")
                .genre("test")
                .runningTime(100)
                .build();


        Movie movie2 = Movie.builder()
                .title("test2")
                .genre("test2")
                .runningTime(100)
                .build();

        String expectedString = movie1 + "\n" + movie2;

        // When
        when(movieService.getAllMovies()).thenReturn(List.of(movie1, movie2));
        String actualString = movieCommands.listMovies();

        // Then
        assertEquals(expectedString, actualString);
    }

    @Test
    public void testListMoviesShouldReturnExpectedStringIfNoMoviesAreFound() {

        // Given
        String expectedString = "There are no movies at the moment";

        // When
        when(movieService.getAllMovies()).thenReturn(Collections.emptyList());
        String actualString = movieCommands.listMovies();

        // Then
        assertEquals(expectedString, actualString);
    }
}
