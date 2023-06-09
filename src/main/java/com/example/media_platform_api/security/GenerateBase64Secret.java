package com.example.media_platform_api.security;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class GenerateBase64Secret {
    public static void main(String[] args) {
        String secret = "myJwtSecret";
        String encodedSecret = Base64.getEncoder().encodeToString(secret.getBytes(StandardCharsets.UTF_8));
        System.out.println("Base64 encoded secret: " + encodedSecret);
    }
}
