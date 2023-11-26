package com.splitly.core.rest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {

  @Value("${application.hello-world}")
  private  String value;

  @GetMapping("/hello-world")
  public String helloWorld() {
    return "Hello World! Environment variable value: " + value;
  }
}
