package com.epam.training.ticketservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.time.LocalDateTime;

@Component
@Entity
@Table(name = "movies")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Screening {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "movie_fk", referencedColumnName = "id")
    private Movie movie;

    @ManyToOne
    @JoinColumn(name = "room_fk", referencedColumnName = "id")
    private Room room;

    @Column(name = "date")
    private LocalDateTime startTime;
}
