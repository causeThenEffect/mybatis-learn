package com.cause.transaction.propagation.model;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class User implements Serializable {
  private Integer id;
  private String name;
}
