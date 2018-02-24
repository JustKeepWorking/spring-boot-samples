package com.github.nduyhai.beanvalidation;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.MapBindingResult;
import org.springframework.validation.SmartValidator;

import java.util.HashMap;

@SpringBootApplication
public class BeanValidationApplication {

    public static void main(String[] args) {
        SpringApplication.run(BeanValidationApplication.class, args);
    }

    @Bean
    ApplicationRunner runner(SmartValidator validator) {
        return args -> {
            final BeanWrapper beanWrapper = new BeanWrapperImpl(new Person());
            beanWrapper.setPropertyValue("name", null);
            beanWrapper.setPropertyValue("email", "no@where");
            beanWrapper.setPropertyValue("age", -1);

            final Person person = (Person) beanWrapper.getWrappedInstance();
            System.out.println(person);

            final MapBindingResult mapBindingResult = new MapBindingResult(new HashMap<>(), "person");
            validator.validate(person, mapBindingResult);
            mapBindingResult.getAllErrors().forEach(e -> System.out.format("%s : %s", e.getObjectName(), e.getDefaultMessage()));
        };
    }
}
