package com.cooperative.voting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.cloud.openfeign.EnableFeignClients;


@SpringBootApplication
@EnableJpaAuditing
@EnableFeignClients(basePackages = "com.cooperative")
public class VotoCooperativoApplication {
  public static void main(String[] args) {
    SpringApplication.run(VotoCooperativoApplication.class, args);
  }
}

