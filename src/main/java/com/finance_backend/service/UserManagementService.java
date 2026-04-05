package com.finance_backend.service;

import com.finance_backend.dto.UserResponse;

import java.util.List;

public interface UserManagementService {
    List<UserResponse> getAllUsers();
    UserResponse updateUserRole(Long userId, String role);
    UserResponse updateUserStatus(Long userId, boolean active);
}