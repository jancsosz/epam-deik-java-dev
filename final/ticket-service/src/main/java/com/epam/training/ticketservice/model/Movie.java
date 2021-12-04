package com.epam.training.ticketservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "movies")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "title", unique = true)
    private String title;

    @Column(name = "genre")
    private String genre;

    @Column(name = "runningTime")
    private int runningTime;

    @OneToMany(mappedBy = "movie")
    private List<Screening> screenings;

    @Override
    public String toString() {
        return this.title + " (" + this.genre + ", " + this.runningTime + " minutes)";
    }
}
