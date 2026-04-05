package com.finance.dashboard.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "email")
})
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String fullName;

    @Email
    @NotBlank
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank
    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.VIEWER;

    @Column(nullable = false)
    private boolean active = true;

    @Column(updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt;

    public User() {}

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override public String getUsername()              { return email; }
    @Override public boolean isAccountNonExpired()     { return true; }
    @Override public boolean isAccountNonLocked()      { return active; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled()               { return active; }

    public Long getId()                  { return id; }
    public String getFullName()          { return fullName; }
    public String getEmail()             { return email; }
    public String getPassword()          { return password; }
    public Role getRole()                { return role; }
    public boolean isActive()            { return active; }
    public LocalDateTime getCreatedAt()  { return createdAt; }
    public LocalDateTime getUpdatedAt()  { return updatedAt; }

    public void setId(Long id)                         { this.id = id; }
    public void setFullName(String fullName)           { this.fullName = fullName; }
    public void setEmail(String email)                 { this.email = email; }
    public void setPassword(String password)           { this.password = password; }
    public void setRole(Role role)                     { this.role = role; }
    public void setActive(boolean active)              { this.active = active; }
    public void setCreatedAt(LocalDateTime v)          { this.createdAt = v; }
    public void setUpdatedAt(LocalDateTime v)          { this.updatedAt = v; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private final User u = new User();
        public Builder fullName(String v)  { u.fullName = v;  return this; }
        public Builder email(String v)     { u.email = v;     return this; }
        public Builder password(String v)  { u.password = v;  return this; }
        public Builder role(Role v)        { u.role = v;      return this; }
        public Builder active(boolean v)   { u.active = v;    return this; }
        public User build()                { return u; }
    }
}
