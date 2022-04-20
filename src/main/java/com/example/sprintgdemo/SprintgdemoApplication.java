package com.example.sprintgdemo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.sprintgdemo.mapper")
public class SprintgdemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SprintgdemoApplication.class, args);
    }

}
