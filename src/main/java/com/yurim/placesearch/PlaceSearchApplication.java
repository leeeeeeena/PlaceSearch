package com.yurim.placesearch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class PlaceSearchApplication {

    public static void main(String[] args) {
        SpringApplication.run(PlaceSearchApplication.class, args);
    }

}
