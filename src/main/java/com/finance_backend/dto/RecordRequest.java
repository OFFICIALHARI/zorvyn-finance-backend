package com.finance_backend.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class RecordRequest {

    @NotNull
    @DecimalMin("0.01")
    private BigDecimal amount;

    @NotBlank
    @Pattern(regexp = "INCOME|EXPENSE", message = "type must be INCOME or EXPENSE")
    private String type;

    @Size(max = 50)
    private String category;

    @PastOrPresent(message = "date cannot be in the future")
    private LocalDate date;

    @Size(max = 500)
    private String description;
}