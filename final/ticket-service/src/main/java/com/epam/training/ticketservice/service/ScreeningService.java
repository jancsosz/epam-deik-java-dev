package com.epam.training.ticketservice.service;

import com.epam.training.ticketservice.exception.CustomException;
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

    private boolean isDateAvailable(LocalDateTime startDate, LocalDateTime endDate,
                                    LocalDateTime startDateToCheck, LocalDateTime endDateToCheck,
                                    int breakTime) {

        return (startDateToCheck.isAfter(endDate.plusMinutes(breakTime)))
                || (endDateToCheck.plusMinutes(breakTime).isBefore(startDate))
                && startDate != startDateToCheck && endDate != endDateToCheck;
    }

    private boolean validateScreening(Screening screening, int breakTime) {

        List<Screening> screeningsInSameRoom = screeningRepository.findByRoom_Name(screening.getRoom().getName());

        if (screeningsInSameRoom.isEmpty()) {
            return true;
        } else {
            return screeningsInSameRoom.stream()
                    .map(savedScreening -> isDateAvailable(savedScreening.getStartTime(),
                            savedScreening.getStartTime().plusMinutes(savedScreening.getMovie().getRunningTime()),
                            screening.getStartTime(),
                            screening.getStartTime().plusMinutes(screening.getMovie().getRunningTime()),
                            breakTime))
                    .filter(boolValue -> !boolValue)
                    .findFirst().orElse(true);
        }
    }

    public Screening mapToScreening(String movieTitle, String roomName, String date)
            throws NotFoundException {

        LocalDateTime parsedDate = LocalDateTime.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

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
            throw new NotFoundException("No screening found with such properties");
        }
    }


    public void createScreening(Screening newScreening) throws CustomException {
        if (validateScreening(newScreening, 0)) {
            if (validateScreening(newScreening, 10)) {
                screeningRepository.save(newScreening);
            } else {
                throw new CustomException("This would start in the break period after another screening in this room");
            }
        } else {
            throw new CustomException("There is an overlapping screening");
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
            throw new NotFoundException("No screening found with such properties");
        }
    }


}
