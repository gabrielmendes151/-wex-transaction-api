package com.wextransactionapi.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequest {

    @Size(max = 50, message = "The description should be less or igual than 50 characters")
    private String description;

    @DecimalMin(value = "0", inclusive = false, message = "The value should be bigger than zero")
    private BigDecimal amount;
}
