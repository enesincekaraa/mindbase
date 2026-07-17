package dev.enes.mindbase;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class MindbaseApplication {

    public static void main(String[] args) {
        SpringApplication.run(MindbaseApplication.class, args);
    }

}
