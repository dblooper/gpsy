package com.gpsy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class GpsyApplication {

    public static void main(String[] args) {
        SpringApplication.run(GpsyApplication.class, args);
    }

}
