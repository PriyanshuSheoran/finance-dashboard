package com.finance.dashboard.dto.request;

import com.finance.dashboard.entity.Role;
import jakarta.validation.constraints.NotNull;

public class UpdateRoleRequest {

    @NotNull(message = "Role is required")
    private Role role;

    public Role getRole()          { return role; }
    public void setRole(Role v)    { this.role = v; }
}
