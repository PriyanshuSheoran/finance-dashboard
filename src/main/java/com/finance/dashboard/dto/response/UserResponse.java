package com.finance.dashboard.dto.response;

import com.finance.dashboard.entity.Role;
import com.finance.dashboard.entity.User;

import java.time.LocalDateTime;

public class UserResponse {

    private Long id;
    private String fullName;
    private String email;
    private Role role;
    private boolean active;
    private LocalDateTime createdAt;

    public UserResponse() {}

    public static UserResponse from(User user) {
        UserResponse r = new UserResponse();
        r.id        = user.getId();
        r.fullName  = user.getFullName();
        r.email     = user.getEmail();
        r.role      = user.getRole();
        r.active    = user.isActive();
        r.createdAt = user.getCreatedAt();
        return r;
    }

    public Long getId()                  { return id; }
    public String getFullName()          { return fullName; }
    public String getEmail()             { return email; }
    public Role getRole()                { return role; }
    public boolean isActive()            { return active; }
    public LocalDateTime getCreatedAt()  { return createdAt; }

    public void setId(Long v)                  { this.id = v; }
    public void setFullName(String v)          { this.fullName = v; }
    public void setEmail(String v)             { this.email = v; }
    public void setRole(Role v)                { this.role = v; }
    public void setActive(boolean v)           { this.active = v; }
    public void setCreatedAt(LocalDateTime v)  { this.createdAt = v; }
}
