package com.ncsu.csc540.parking.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppUser {

    private String userName;
    private String encrytedPassword;
    private Long univid;

}