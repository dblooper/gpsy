package com.gpsy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

//@SpringBootApplication
//@EnableAspectJAutoProxy
//public class GpsyApplication {
//
//    public static void main(String[] args) {
//        SpringApplication.run(GpsyApplication.class, args);
//    }
//
//}

//deployment
@SpringBootApplication
@EnableAspectJAutoProxy
public class GpsyApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(GpsyApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(GpsyApplication.class);
    }
}