package com.ncsu.csc540.parking.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Coffees {

    private String COF_NAME;
    private int SUP_ID;
    private float PRICE;
    private int SALES;
    private int TOTAL;

}
