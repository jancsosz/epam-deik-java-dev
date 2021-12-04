package com.epam.training.ticketservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "movies")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Screening {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "movie_fk", referencedColumnName = "id")
    private Movie movie;

    @ManyToOne
    @JoinColumn(name = "room_fk", referencedColumnName = "id")
    private Room room;

    @Column(name = "date")
    private LocalDateTime startTime;

    @Override
    public String toString() {
        return movie.getTitle() + " (" + movie.getGenre() + ", " + movie.getRunningTime() +
                " minutes), screened in room " + room.getName() + ", at " + startTime;
    }
}
