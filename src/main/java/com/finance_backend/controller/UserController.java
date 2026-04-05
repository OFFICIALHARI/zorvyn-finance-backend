package com.finance_backend.controller;

import com.finance_backend.dto.UpdateUserRoleRequest;
import com.finance_backend.dto.UpdateUserStatusRequest;
import com.finance_backend.dto.UserResponse;
import com.finance_backend.service.UserManagementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class UserController {

    private final UserManagementService userManagementService;

    @GetMapping
    public List<UserResponse> listUsers() {
        return userManagementService.getAllUsers();
    }

    @PatchMapping("/{id}/role")
    public UserResponse updateRole(@PathVariable Long id,
                                   @Valid @RequestBody UpdateUserRoleRequest request) {
        return userManagementService.updateUserRole(id, request.getRole());
    }

    @PatchMapping("/{id}/status")
    public UserResponse updateStatus(@PathVariable Long id,
                                     @Valid @RequestBody UpdateUserStatusRequest request) {
        return userManagementService.updateUserStatus(id, request.getActive());
    }
}