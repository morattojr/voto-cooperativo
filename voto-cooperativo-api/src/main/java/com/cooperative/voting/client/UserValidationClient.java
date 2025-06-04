package com.cooperative.voting.client;

import com.cooperative.voting.client.dto.UserDTO;
import com.userservice.model.UserStatus;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", url = "${user.service.url}")
public interface UserValidationClient {

  @GetMapping("/users/{userId}")
  UserDTO getUser(@PathVariable("userId") String userId);

}