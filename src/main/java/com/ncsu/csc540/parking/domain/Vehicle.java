package com.ncsu.csc540.parking.domain;

import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Vehicle implements Serializable{
    private String manufacturer;
    private String model;
    private String year;
    private String color;
    private String license_plate_no;
}