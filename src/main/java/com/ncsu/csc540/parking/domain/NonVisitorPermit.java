package com.ncsu.csc540.parking.domain;

import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class NonVisitorPermit implements Serializable{
    private String permit_id;
    private String license_plate_no;
    private java.sql.Timestamp start_time;
    private java.sql.Timestamp exp_time;
    private String zone_id;
    private String space_type;
    private Long univid;
}



