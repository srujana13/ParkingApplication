package com.ncsu.csc540.parking.domain;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CitationVO {
    private Long cNo;
    private String licenseNumber;
    private String model;
    private String color;
    private java.sql.Date cDate;
    private String lot;
    private java.sql.Timestamp time;
    private String violationCategory;
    private double fee;
    private java.sql.Timestamp due;
    private String status;
}
