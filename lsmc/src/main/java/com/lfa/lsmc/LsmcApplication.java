package com.lfa.lsmc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableScheduling
@EnableTransactionManagement
@EnableConfigurationProperties
public class LsmcApplication {

    public static void main(String[] args) {
        SpringApplication.run(LsmcApplication.class, args);
    }

}
