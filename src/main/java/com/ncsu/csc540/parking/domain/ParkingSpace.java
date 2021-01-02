package com.ncsu.csc540.parking.domain;

import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ParkingSpace implements Serializable{
    private Number space_no;
    private String parking_lot_name;
    private String space_type;
    private String zone;
    private Integer occupied;
}


