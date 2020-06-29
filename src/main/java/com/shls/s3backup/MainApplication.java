package com.shls.s3backup;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class MainApplication  {

    public MainApplication() {
    }

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }

}
