package com.finance.dashboard.service;

import com.finance.dashboard.dto.request.UpdateRoleRequest;
import com.finance.dashboard.dto.response.UserResponse;
import com.finance.dashboard.entity.Role;

import java.util.List;

public interface UserService {
    List<UserResponse> getAllUsers();
    UserResponse getUserById(Long id);
    UserResponse updateRole(Long id, UpdateRoleRequest request);
    UserResponse setActiveStatus(Long id, boolean active);
    List<UserResponse> getUsersByRole(Role role);
    void deleteUser(Long id);
}
