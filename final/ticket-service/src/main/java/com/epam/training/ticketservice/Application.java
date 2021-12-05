package com.epam.training.ticketservice;

import com.epam.training.ticketservice.model.CliUser;
import com.epam.training.ticketservice.repository.CliUserRepository;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.shell.jline.PromptProvider;

@SpringBootApplication(scanBasePackages = "com.epam.training.ticketservice")
public class Application implements CommandLineRunner {

    @Autowired
    CliUserRepository cliUserRepository;

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

        System.out.println("run");
        cliUserRepository.save(CliUser.builder().username("admin").password("admin").build());

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
