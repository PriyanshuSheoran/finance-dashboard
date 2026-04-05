package com.finance.dashboard.dto.request;

import com.finance.dashboard.entity.Role;
import jakarta.validation.constraints.*;

public class RegisterRequest {

    @NotBlank(message = "Full name is required")
    @Size(min = 2, max = 100)
    private String fullName;

    @Email(message = "Must be a valid email")
    @NotBlank
    private String email;

    @NotBlank
    @Size(min = 6, max = 100, message = "Password must be 6-100 characters")
    private String password;

    private Role role = Role.VIEWER;

    public String getFullName()            { return fullName; }
    public void setFullName(String v)      { this.fullName = v; }
    public String getEmail()               { return email; }
    public void setEmail(String v)         { this.email = v; }
    public String getPassword()            { return password; }
    public void setPassword(String v)      { this.password = v; }
    public Role getRole()                  { return role; }
    public void setRole(Role v)            { this.role = v; }
}
