package com.ncsu.csc540.parking.config;


import com.ncsu.csc540.parking.controller.AppController;
import lombok.AllArgsConstructor;

public class AppException extends RuntimeException {

    private String message;

    public AppException(String message){
        super(message);
    }

}
