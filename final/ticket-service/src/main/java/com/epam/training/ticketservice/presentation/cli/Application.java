package com.epam.training.ticketservice.presentation.cli;

import com.epam.training.ticketservice.model.Movie;
import com.epam.training.ticketservice.model.Role;
import com.epam.training.ticketservice.model.Room;
import com.epam.training.ticketservice.model.User;
import com.epam.training.ticketservice.repository.MovieRepository;
import com.epam.training.ticketservice.repository.RoomRepository;
import com.epam.training.ticketservice.service.MovieService;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.shell.jline.PromptProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication(scanBasePackages = "com.epam.training.ticketservice")
public class Application implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public PromptProvider myPromptProvider() {
        return () -> new AttributedString("Ticket service>",
                AttributedStyle.DEFAULT.foreground(AttributedStyle.RED));
    }

    @Override
    public void run(String... params) {
        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword("admin");
        admin.setRoles(new ArrayList<>(List.of(Role.ROLE_ADMIN)));

        /*movieRepository.save(Movie.builder()
                .title("movie")
                .genre("asd")
                .runningTime(120)
                .build());

        roomRepository.save(Room.builder()
                .name("room")
                .rows(20)
                .cols(30)
                .build());*/
    }
}
