package com.userservice.controller;

import com.userservice.dto.UserInfoResponse;
import com.userservice.model.UserStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@Tag(name = "User Validation", description = "Validate user voting eligibility")
public class UserController {

  @Operation(summary = "Check user voting status by CPF")
  @GetMapping("/{cpf}")
  public ResponseEntity<UserInfoResponse> getUserStatus(@PathVariable String cpf) {
    boolean isValid = cpf != null && cpf.matches("\\d{11}") && Integer.parseInt(cpf.substring(10)) % 2 == 0;
    UserStatus status = isValid ? UserStatus.ABLE_TO_VOTE : UserStatus.UNABLE_TO_VOTE;
    UserInfoResponse response = UserInfoResponse.builder().status(status).build();
    return ResponseEntity.ok(response);
  }
}