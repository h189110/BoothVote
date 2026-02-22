package com.birkt.boothvote;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BoothVoteApplication {

    public static void main(String[] args) {
        SpringApplication.run(BoothVoteApplication.class, args);
    }

}
