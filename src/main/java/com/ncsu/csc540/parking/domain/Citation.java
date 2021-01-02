package com.ncsu.csc540.parking.domain;

import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Citation implements Serializable {

  private Long cNo;
  private String licenseNumber;
  private String model;
  private String color;
  private java.sql.Timestamp cDate;
  private String cDate_String;
  private String lot;
  private int violationId;
  private java.sql.Timestamp due;
  private String status =  "UNPAID";


}
