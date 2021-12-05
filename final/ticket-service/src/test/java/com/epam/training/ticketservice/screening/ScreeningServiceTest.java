package com.epam.training.ticketservice.screening;

import com.epam.training.ticketservice.exception.CustomException;
import com.epam.training.ticketservice.model.Movie;
import com.epam.training.ticketservice.model.Room;
import com.epam.training.ticketservice.model.Screening;
import com.epam.training.ticketservice.repository.ScreeningRepository;
import com.epam.training.ticketservice.service.MovieService;
import com.epam.training.ticketservice.service.RoomService;
import com.epam.training.ticketservice.service.ScreeningService;
import javassist.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ScreeningServiceTest {

    @Mock
    private ScreeningRepository screeningRepository;

    @Mock
    private MovieService movieService;

    @Mock
    private RoomService roomService;

    @InjectMocks
    private ScreeningService screeningService;

    private Room testRoom;
    private Movie testMovie;
    private Screening testScreening;
    private List<Screening> testList;
    private LocalDateTime testDate;


    @BeforeEach
    void setUp() {
        testRoom = Room.builder()
                .name("testRoom")
                .cols(10)
                .rows(10)
                .build();

        testMovie = Movie.builder()
                .title("testMovie")
                .genre("test")
                .runningTime(100)
                .build();

        testDate = LocalDateTime.of(2000, Month.APRIL, 30, 9, 50);

        testScreening = Screening.builder()
                .room(testRoom)
                .movie(testMovie)
                .startTime(testDate)
                .build();

        testList = List.of(Screening.builder()
                .movie(testMovie)
                .room(testRoom)
                .startTime(LocalDateTime.of(2000, Month.APRIL, 30, 10, 10))
                .build());

    }


    @Test
    public void testGetAllScreeningsShouldReturnListOfScreenings() {

        //Given
        List<Screening> expectedList = testList;

        //When
        when(screeningRepository.findAll()).thenReturn(expectedList);
        List<Screening> actualList = screeningService.getAllScreenings();

        //Then
        assertEquals(expectedList, actualList);
    }

    @Test
    public void testGetScreeningByPropertiesShouldReturnScreeningIfPropertiesAreValid() throws NotFoundException {

        // Given
        String roomName = testRoom.getName();
        String title = testMovie.getTitle();
        LocalDateTime date = testScreening.getStartTime();
        String dateAsString = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));


        // When
        when(screeningRepository.findByMovie_TitleAndRoom_NameAndStartTime(
                title, roomName, date)).thenReturn(testScreening);

        Screening actualScreening = screeningService.getScreeningByProperties(title, roomName, dateAsString);


        // Then
        assertEquals(testScreening, actualScreening);


    }

    @Test
    public void testGetScreeningByPropertiesShouldThrowNotFoundExceptionIfScreeningDoesNotExist() throws NotFoundException {

        // Given
        String roomName = testRoom.getName();
        String title = testMovie.getTitle();
        LocalDateTime date = testScreening.getStartTime();
        String dateAsString = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));


        // When
        when(screeningRepository.findByMovie_TitleAndRoom_NameAndStartTime(
                title, roomName, date)).thenReturn(null);


        // Then
        assertThrows(NotFoundException.class,
                () -> screeningService.getScreeningByProperties(title, roomName, dateAsString));

    }

    @Test
    public void testCreateScreeningShouldSucceedWhenThereIsNoConflictIngDate() throws CustomException {

        // Given
        testScreening.setStartTime(LocalDateTime.of(1999, Month.APRIL, 10, 10, 10));
        testList.get(0).setStartTime(LocalDateTime.of(2000, Month.APRIL, 20, 20, 20));

        // When
        when(screeningRepository.findByRoom_Name(testRoom.getName())).thenReturn(testList);
        screeningService.createScreening(testScreening);

        // Then
        verify(screeningRepository, times(1)).save(testScreening);
    }

    @Test
    public void testCreateScreeningShouldNotSaveWhenThereIsConflictingDate() {

        // Given
        LocalDateTime time = LocalDateTime.of(1999, Month.APRIL, 10, 10, 10);
        testScreening.setStartTime(time);
        testList.get(0).setStartTime(time);

        // When
        when(screeningRepository.findByRoom_Name(testRoom.getName())).thenReturn(testList);

        // Then
        assertThrows(CustomException.class, () -> screeningService.createScreening(testScreening));
        verify(screeningRepository, times(0)).save(testScreening);
    }

    @Test
    public void testCreateScreeningShouldThrowCustomExceptionIfNewScreeningWouldStartInBreakTime() {

        // Given
        LocalDateTime time = LocalDateTime.of(1999, Month.APRIL, 10, 10, 10);
        int movieLength = testMovie.getRunningTime();
        testScreening.setStartTime(time.plusMinutes(movieLength + 1));
        testList.get(0).setStartTime(time);

        // When
        when(screeningRepository.findByRoom_Name(testRoom.getName())).thenReturn(testList);

        // Then
        assertThrows(CustomException.class, () -> screeningService.createScreening(testScreening));
        verify(screeningRepository, times(0)).save(testScreening);
    }

    @Test
    public void testCreateScreeningShouldSucceedWhenThereAreNoScreeningsInTheRoom() throws CustomException {

        // Given


        // When
        when(screeningRepository.findByRoom_Name(testRoom.getName())).thenReturn(Collections.emptyList());
        screeningService.createScreening(testScreening);

        // Then
        verify(screeningRepository, times(1)).save(testScreening);
    }

    @Test
    public void testDeleteScreeningShouldDeleteScreeningWhenExists() throws NotFoundException {

        //Given


        //When
        when(screeningRepository.existsByMovie_TitleAndRoom_NameAndStartTime(anyString(), anyString(), any(LocalDateTime.class)))
                .thenReturn(true);
        screeningService.deleteScreening(testScreening);

        //Then
        verify(screeningRepository, times(1))
                .deleteByMovie_TitleAndRoom_NameAndStartTime(anyString(), anyString(), any(LocalDateTime.class));
    }


    @Test
    public void testDeleteScreeningShouldThrowNotFoundExceptionWhenExistByReturnsFalse() {

        //Given


        //When
        when(screeningRepository.existsByMovie_TitleAndRoom_NameAndStartTime(anyString(), anyString(), any(LocalDateTime.class)))
                .thenReturn(false);

        //Then
        assertThrows(NotFoundException.class,
                () -> screeningService.deleteScreening(testScreening));

        verify(screeningRepository, times(0))
                .deleteByMovie_TitleAndRoom_NameAndStartTime(anyString(), anyString(), any(LocalDateTime.class));
    }

    @Test
    public void testMapToScreeningShouldReturnWhenPropertiesAreValid() throws NotFoundException {

        // Given
        String movieTitle = testMovie.getTitle();
        String roomName = testRoom.getName();
        String date = testDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        Screening expectedScreening = testScreening;

        // When
        when(movieService.findByTitle(movieTitle)).thenReturn(testMovie);
        when(roomService.findByName(roomName)).thenReturn(testRoom);

        Screening actualScreening = screeningService.mapToScreening(movieTitle, roomName, date);

        // Then
        assertEquals(expectedScreening, actualScreening);

    }

}
