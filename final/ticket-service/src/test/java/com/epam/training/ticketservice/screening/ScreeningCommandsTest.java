package com.epam.training.ticketservice.screening;

import com.epam.training.ticketservice.commands.ScreeningCommands;
import com.epam.training.ticketservice.exception.CustomException;
import com.epam.training.ticketservice.model.Movie;
import com.epam.training.ticketservice.model.Room;
import com.epam.training.ticketservice.model.Screening;
import com.epam.training.ticketservice.repository.ScreeningRepository;
import com.epam.training.ticketservice.service.ScreeningService;
import javassist.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ScreeningCommandsTest {

    @Mock
    ScreeningService screeningService;

    @Mock
    ScreeningRepository screeningRepository;

    @InjectMocks
    ScreeningCommands screeningCommands;

    private String movieTitle;
    private String roomName;
    private String date;
    private Screening screening;

    @BeforeEach
    void setUp() {
        movieTitle = "test";
        roomName = "test";
        date = "2021-11-11 11:11";
        screening = Screening.builder()
                .movie(Movie.builder()
                        .title(movieTitle).build())
                .room(Room.builder()
                        .name(roomName).build())
                .startTime(LocalDateTime.of(2021, 11, 11, 11, 11)).build();
    }

    @Test
    public void testCreateScreening() throws Exception {

        // Given


        // When
        when(screeningService.mapToScreening(movieTitle, roomName, date)).thenReturn(screening);

        screeningCommands.createScreening(movieTitle, roomName, date);

        // Then
        verify(screeningService, times(1)).createScreening(any(Screening.class));
    }


    @Test
    public void testCreateScreeningShouldNotSaveScreeningWhenCustomExceptionIsCaught()
            throws Exception {

        // Given


        // When
        when(screeningService.mapToScreening(movieTitle, roomName, date)).thenReturn(screening);
        doThrow(CustomException.class).when(screeningService).createScreening(any(Screening.class));

        screeningCommands.createScreening(movieTitle, roomName, date);

        // Then
        verify(screeningRepository, times(0)).save(any(Screening.class));
    }

    @Test
    public void testCreateScreeningShouldNotSaveScreeningIfNotFoundExceptionIsCaught() throws NotFoundException {

        // Given


        // When

        doThrow(NotFoundException.class).when(screeningService).mapToScreening(movieTitle, roomName, date);

        screeningCommands.createScreening(movieTitle, roomName, date);

        // Then
        verify(screeningRepository, times(0)).save(any(Screening.class));
    }


    @Test
    public void testDeleteScreeningShouldDeleteScreeningWhenFound() throws NotFoundException {

        // Given


        // When
        when(screeningService.mapToScreening(movieTitle, roomName, date)).thenReturn(screening);
        screeningCommands.deleteScreening(movieTitle, roomName, date);

        // Then
        verify(screeningService, times(1))
                .deleteScreening(any(Screening.class));
    }

    @Test
    public void testDeleteScreeningShouldNotDeleteScreeningWhenNotFoundExceptionIsCaught()
            throws NotFoundException {

        // Given


        // When
        doThrow(NotFoundException.class)
                .when(screeningService)
                .mapToScreening(movieTitle, roomName, date);

        screeningCommands.deleteScreening(movieTitle, roomName, date);

        // Then
        verify(screeningRepository, times(0))
                .deleteByMovie_TitleAndRoom_NameAndStartTime(
                        anyString(),
                        anyString(),
                        any(LocalDateTime.class));
    }

    @Test
    public void testListScreeningsShouldReturnExpectedStringWhenNoScreeningsAreFound() {

        // Given
        String expectedString = "There are no screenings";

        // When
        when(screeningService.getAllScreenings()).thenReturn(Collections.emptyList());
        String actualString = screeningCommands.listScreenings();

        // Then
        assertEquals(expectedString, actualString);
    }

    @Test
    public void testListScreeningsShouldReturnWithFoundScreeningsString() {

        // Given
        Screening testScreening1 = Screening.builder()
                .room(new Room())
                .movie(new Movie())
                .startTime(LocalDateTime.now())
                .build();


        Screening testScreening2 = Screening.builder()
                .room(new Room())
                .movie(new Movie())
                .startTime(LocalDateTime.now())
                .build();

        String expectedString = testScreening1 + "\n" + testScreening2;

        // When
        when(screeningService.getAllScreenings()).thenReturn(List.of(testScreening1, testScreening2));
        String actualString = screeningCommands.listScreenings();

        // Then
        assertEquals(expectedString, actualString);
    }


}
