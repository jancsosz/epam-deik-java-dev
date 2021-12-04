package com.epam.training.ticketservice.service;

import com.epam.training.ticketservice.model.Screening;
import com.epam.training.ticketservice.repository.ScreeningRepository;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScreeningService {

    private final ScreeningRepository screeningRepository;
    private final MovieService movieService;
    private final RoomService roomService;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private static final String SCREENING_NOT_FOUND = "No screening found with such properties";
    private static final String BREAK_TIME_CONFLICT =
            "This would start in the break period after another screening in this room";
    private static final String OVERLAPPING_TIME = "There is an overlapping screening";

    private boolean isDateAvailable(LocalDateTime startDate, LocalDateTime endDate,
                                    LocalDateTime startDateToCheck, LocalDateTime endDateToCheck,
                                    int breakTime) {

        return !((startDateToCheck.isBefore(endDate.plusMinutes(breakTime)) && startDateToCheck.isAfter(startDate))
                || (endDateToCheck.isBefore(endDate) && endDateToCheck.isAfter(startDate)))
                && startDate != startDateToCheck && endDate != endDateToCheck;
    }

    private boolean validateScreening(Screening screening) {

        List<Screening> screeningsInSameRoom = screeningRepository.findByRoom_Name(screening.getRoom().getName());

        if (screeningsInSameRoom.isEmpty()) {
            return true;
        } else {
            return screeningsInSameRoom.stream()
                    .map(y -> isDateAvailable(y.getStartTime(),
                            y.getStartTime().plusMinutes(y.getMovie().getRunningTime()),
                            screening.getStartTime(),
                            screening.getStartTime().plusMinutes(screening.getMovie().getRunningTime()), 0))
                    .filter(boolValue -> !boolValue)
                    .findFirst().orElse(true);
        }
    }

    private boolean validateScreening(Screening screening, int breakTime) {

        List<Screening> screeningsInSameRoom = screeningRepository.findByRoom_Name(screening.getRoom().getName());

        if (screeningsInSameRoom.isEmpty()) {
            return true;
        } else {
            return screeningsInSameRoom.stream()
                    .map(y -> isDateAvailable(y.getStartTime(),
                            y.getStartTime().plusMinutes(y.getMovie().getRunningTime()),
                            screening.getStartTime(),
                            screening.getStartTime().plusMinutes(screening.getMovie().getRunningTime()), breakTime))
                    .filter(boolValue -> !boolValue)
                    .findFirst().orElse(true);
        }
    }

    public Screening mapToScreening(String movieTitle, String roomName, String date)
            throws NotFoundException {

        LocalDateTime parsedDate = LocalDateTime.parse(date, formatter);

        return Screening.builder()
                .movie(movieService.findByTitle(movieTitle))
                .room(roomService.findByName(roomName))
                .startTime(parsedDate)
                .build();
    }

    public List<Screening> getAllScreenings() {
        return screeningRepository.findAll();
    }

    public Screening getScreeningByProperties(String movieTitle,
                                              String roomName,
                                              String date) throws NotFoundException {
        Screening screening =
                screeningRepository.findByMovie_TitleAndRoom_NameAndStartTime(
                        movieTitle,
                        roomName,
                        LocalDateTime.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));

        if (screening != null) {
            return screening;
        } else {
            throw new NotFoundException(SCREENING_NOT_FOUND);
        }
    }


    public void createScreening(Screening newScreening) throws Exception {
        if (validateScreening(newScreening)) {
            if (validateScreening(newScreening, 10)) {
                screeningRepository.save(newScreening);
                System.out.println(screeningRepository.findAll());
            } else {
                throw new Exception(BREAK_TIME_CONFLICT);
            }
        } else {
            throw new Exception(OVERLAPPING_TIME);
        }
    }

    public void deleteScreening(Screening screening) throws NotFoundException {
        if (screeningRepository.existsByMovie_TitleAndRoom_NameAndStartTime(
                screening.getMovie().getTitle(),
                screening.getRoom().getName(),
                screening.getStartTime())) {

            screeningRepository.deleteByMovie_TitleAndRoom_NameAndStartTime(
                    screening.getMovie().getTitle(),
                    screening.getRoom().getName(),
                    screening.getStartTime());

        } else {
            throw new NotFoundException(SCREENING_NOT_FOUND);
        }
    }


}
