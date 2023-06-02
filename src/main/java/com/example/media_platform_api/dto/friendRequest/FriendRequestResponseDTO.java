package com.example.media_platform_api.dto.friendRequest;

import lombok.Data;

@Data
public class FriendRequestResponseDTO {
    private Long senderId;
    private String senderUsername;
    private String email;
}