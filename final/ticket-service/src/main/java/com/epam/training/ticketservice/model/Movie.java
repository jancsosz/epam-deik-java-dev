package com.epam.training.ticketservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.*;

@Component
@Entity
@Table(name = "movies")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Movie {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "title", unique = true)
    private String title;

    @Column(name = "genre")
    private String genre;

    @Column(name = "runningTime")
    private int runningTime;

    @Override
    public String toString() {
        return this.title + " (" + this.genre + ", " + this.runningTime + " minutes)";
    }
}
