package com.ncsu.csc540.parking.domain;

import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ZoneDesignation implements Serializable {
    private String zone;
    private String lot_name;
    private Long from;
    private Long to;
}

