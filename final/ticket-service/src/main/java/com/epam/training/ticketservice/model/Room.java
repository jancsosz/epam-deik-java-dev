package com.epam.training.ticketservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "room")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "rows", nullable = false)
    private int rows;

    @Column(name = "cols", nullable = false)
    private int cols;

    @OneToMany(mappedBy = "room")
    private List<Screening> screenings;

    @Override
    public String toString() {
        return "Room " + this.name + " with " + this.cols * this.rows + " seats, " + this.rows + " rows and " + this.cols + " columns";
    }
}
