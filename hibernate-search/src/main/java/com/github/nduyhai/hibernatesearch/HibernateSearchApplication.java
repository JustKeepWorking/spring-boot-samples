package com.github.nduyhai.hibernatesearch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 *  May be error when start app
 *  The exception is logged by Hibernate and is out of Boot’s control
 *  Should ignore this...
 *  {@linkplain https://stackoverflow.com/questions/46493500/hibernate-with-c3p0-createclob-is-not-yet-implemented}
 *  {@link https://github.com/spring-projects/spring-boot/issues/12007#issuecomment-369388646}
 */
@SpringBootApplication
public class HibernateSearchApplication {

    public static void main(String[] args) {
        SpringApplication.run(HibernateSearchApplication.class, args);
    }


}



