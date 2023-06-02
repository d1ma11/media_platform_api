package com.example.media_platform_api.dto.user;

import lombok.Data;

@Data
public class UserResponseDTO {
    private Long id;
    private String email;
    private String username;
    private int friendsCount;
    private boolean active;
}