package com.epam.training.ticketservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Component
@Entity
@Table(name = "room")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Room {

    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private int rows;
    private int cols;

    @Override
    public String toString() {
        return "Room " + this.name + " with " + this.cols * this.rows + " seats, " + this.rows + " rows and " + this.cols + " columns";
    }
}
