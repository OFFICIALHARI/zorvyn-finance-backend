package com.finance_backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UpdateUserRoleRequest {

    @NotBlank
    @Pattern(regexp = "VIEWER|ANALYST|ADMIN", message = "role must be VIEWER, ANALYST, or ADMIN")
    private String role;
}