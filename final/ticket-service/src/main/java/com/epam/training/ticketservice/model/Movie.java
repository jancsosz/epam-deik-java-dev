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
import javax.persistence.OneToMany;
import javax.persistence.GenerationType;
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

    @Column(name = "genre", nullable = false)
    private String genre;

    @Column(name = "running_time", nullable = false)
    private int runningTime;

    @OneToMany(mappedBy = "movie")
    private List<Screening> screenings;

    @Override
    public String toString() {
        return this.title + " (" + this.genre + ", " + this.runningTime + " minutes)";
    }
}
