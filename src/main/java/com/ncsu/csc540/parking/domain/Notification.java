package com.ncsu.csc540.parking.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Notification {

  private String nId;
  private String message;
  private String issuedTo;

}
