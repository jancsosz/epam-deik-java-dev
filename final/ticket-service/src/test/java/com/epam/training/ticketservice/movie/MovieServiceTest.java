package com.epam.training.ticketservice.movie;

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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MovieServiceTest {

    private Movie testMovie;

    @Mock
    private MovieRepository movieRepository;

    @InjectMocks
    MovieService movieService;

    @BeforeEach
    void setUp() {
        testMovie = Movie.builder()
                .title("test")
                .genre("test")
                .runningTime(1)
                .build();
    }

    @Test
    public void testGetAllMoviesShouldReturnListOfMovies() {

        //Given
        List<Movie> expectedList = List.of(testMovie);

        //When
        when(movieRepository.findAll()).thenReturn(expectedList);
        List<Movie> actualList = movieService.getAllMovies();

        //Then
        assertEquals(expectedList, actualList);
    }

    @Test
    public void testFindByTitleShouldReturnMovieWhenFound() throws NotFoundException {

        // Given


        // When
        when(movieRepository.findByTitle(testMovie.getTitle())).thenReturn(testMovie);
        Movie actualMovie = movieService.findByTitle(testMovie.getTitle());

        // Then
        assertEquals(testMovie, actualMovie);
    }

    @Test
    public void testFindByTitleShouldNotReturnWhenNotFoundExceptionIsThrown() {

    }

    @Test
    public void testCreateMovieShouldPersistMovieWhenDoesntExist() throws CustomException {

        //Given

        //When
        movieService.createMovie(testMovie);

        //Then
        verify(movieRepository, times(1)).save(testMovie);
    }

    @Test
    public void testCreateMovieShouldThrowCustomExceptionWhenMovieAlreadyExists() {

        // Given


        // When
        when(movieRepository.existsByTitle(anyString())).thenReturn(true);

        // Then
        assertThrows(CustomException.class, () -> movieService.createMovie(testMovie));
        verify(movieRepository, times(0)).save(testMovie);
    }

    @Test
    public void testUpdateMovieShouldUpdateMovieWhenFound() throws NotFoundException {

        //Given


        //When
        when(movieRepository.existsByTitle(testMovie.getTitle())).thenReturn(true);
        movieService.updateMovie(testMovie);

        //Then
        verify(movieRepository, times(1)).update(testMovie.getTitle(),
                testMovie.getGenre(),
                testMovie.getRunningTime());
    }

    @Test
    public void testUpdateMovieShouldThrowNotFoundExceptionWhenMovieDoesntExists() {

        //Given


        //When
        when(movieRepository.existsByTitle(testMovie.getTitle())).thenReturn(false);

        //Then
        assertThrows(NotFoundException.class, () -> movieService.updateMovie(testMovie));
        verify(movieRepository, times(0)).update(testMovie.getTitle(),
                testMovie.getGenre(),
                testMovie.getRunningTime());
    }

    @Test
    public void testDeleteMovieShouldDeleteMovieWhenFound() throws NotFoundException {

        //Given


        //When
        when(movieRepository.existsByTitle(testMovie.getTitle())).thenReturn(true);
        movieService.deleteMovie(testMovie.getTitle());

        //Then
        verify(movieRepository, times(1)).deleteByTitle(testMovie.getTitle());
    }

    @Test
    public void testDeleteMovieShouldThrowNotFoundExceptionWhenMovieDoesntExists() {

        //Given


        //When
        when(movieRepository.existsByTitle(testMovie.getTitle())).thenReturn(false);

        //Then
        assertThrows(NotFoundException.class, () -> movieService.deleteMovie(testMovie.getTitle()));
        verify(movieRepository, times(0)).deleteByTitle(testMovie.getTitle());
    }

    @Test
    public void testMapToMovieShouldReturnMovieWhenPropertiesArePresent() {
        String title = "room";
        String genre = "asd";
        int runningTime = 120;
        Movie expectedRoom = Movie.builder()
                .title(title)
                .genre(genre)
                .runningTime(runningTime)
                .build();

        // When
        Movie actualMovie = movieService.mapToMovie(title, genre, runningTime);

        // Then
        assertEquals(expectedRoom, actualMovie);

    }


}
