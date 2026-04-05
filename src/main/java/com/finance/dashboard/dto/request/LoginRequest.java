package com.finance.dashboard.dto.request;

import jakarta.validation.constraints.*;

public class LoginRequest {

    @Email(message = "Must be a valid email")
    @NotBlank
    private String email;

    @NotBlank
    private String password;

    public String getEmail()              { return email; }
    public void setEmail(String email)    { this.email = email; }
    public String getPassword()           { return password; }
    public void setPassword(String pw)    { this.password = pw; }
}
