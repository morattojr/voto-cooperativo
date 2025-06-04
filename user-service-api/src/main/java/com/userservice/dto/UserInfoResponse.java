package com.userservice.dto;

import com.userservice.model.UserStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserInfoResponse {
  private UserStatus status;
}