package com.wextransactionapi.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.wextransactionapi.models.Transaction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionResponse {

    private Long id;

    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    private BigDecimal amountUSD;

    private BigDecimal exchangeRate;

    private BigDecimal convertedAmount;

    private String targetCurrency;

    public TransactionResponse(Transaction transaction, BigDecimal exchangeRate, BigDecimal convertedAmount, String targetCurrency) {
        this.id = transaction.getId();
        this.description = transaction.getDescription();
        this.createdAt = transaction.getCreatedAt();
        this.amountUSD = transaction.getAmountUSD();
        this.exchangeRate = exchangeRate;
        this.convertedAmount = convertedAmount;
        this.targetCurrency = targetCurrency;
    }

    public static TransactionResponse from(Transaction transaction) {
        TransactionResponse response = new TransactionResponse();
        response.setId(transaction.getId());
        response.setDescription(transaction.getDescription());
        response.setAmountUSD(transaction.getAmountUSD());
        response.setCreatedAt(transaction.getCreatedAt());
        return response;
    }

}
