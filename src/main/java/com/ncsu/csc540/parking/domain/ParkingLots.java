package com.ncsu.csc540.parking.domain;

import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ParkingLots implements Serializable {
    private String name;
    private String address;
    private Long space_count;
    private Long visitor_space_count;
    private Long beginning_space_number;
    private String zone;
}


