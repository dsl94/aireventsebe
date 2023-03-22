package com.airevents.security.dto;

public class JwtAuthenticationDto {

    private String email;
    private String password;

    public JwtAuthenticationDto() {
    }

    public JwtAuthenticationDto(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setUsername(String username) {
        this.email = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

