package com.epam.training.ticketservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "screenings")
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

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Override
    public String toString() {
        return movie
                + ", screened in room " + room.getName() + ", at "
                + startTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }
}
