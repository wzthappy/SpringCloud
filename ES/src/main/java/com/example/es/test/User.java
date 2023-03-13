package com.example.es.test;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class User {
  private String name;
  private String sex;
  private Integer age;
}
