package com.github.nduyhai.hibernatesearch;

import de.codecentric.boot.admin.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableAdminServer
@SpringBootApplication
public class HibernateSearchApplication {

    public static void main(String[] args) {
        SpringApplication.run(HibernateSearchApplication.class, args);
    }
}



