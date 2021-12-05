package com.epam.training.ticketservice;

import com.epam.training.ticketservice.repository.CliUserRepository;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.shell.jline.PromptProvider;

@SpringBootApplication(scanBasePackages = "com.epam.training.ticketservice")
public class Application {

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
}
