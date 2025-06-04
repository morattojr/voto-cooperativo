package com.cooperative.voting.client.dto;

import com.userservice.model.UserStatus;
import lombok.Data;

@Data
public class UserDTO {
    private String id;
    private String name;
    private UserStatus status;
}
