package com.epam.training.ticketservice.repository;

import com.epam.training.ticketservice.model.Screening;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ScreeningRepository extends JpaRepository<Screening, Long> {

    boolean existsByMovie_TitleAndRoom_NameAndStartTime(String movieTitle, String roomName, LocalDateTime date);

    Screening findByMovie_TitleAndRoom_NameAndStartTime(String movieTitle, String roomName, LocalDateTime date);

    List<Screening> findByRoom_Name(String roomName);

    @Transactional
    void deleteByMovie_TitleAndRoom_NameAndStartTime(String movieTitle, String roomName, LocalDateTime date);
}
