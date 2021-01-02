package com.ncsu.csc540.parking.domain;

import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class VisitorPermit implements Serializable{
    private String permit_id;
    private String license_plate_no;
    private java.sql.Timestamp start_time;
    private java.sql.Timestamp exp_time;
    private String startTime;
    private Integer duration;
    private String zone_id;
    private String space_type;
    private Long overage;
    private Integer space_no;
    private String lot_name;
    private String contact_no;
}



