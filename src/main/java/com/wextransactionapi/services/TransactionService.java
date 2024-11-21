package com.wextransactionapi.services;

import com.wextransactionapi.dto.ExchangeRateResponse;
import com.wextransactionapi.dto.TransactionRequest;
import com.wextransactionapi.dto.TransactionResponse;
import com.wextransactionapi.execeptions.NotFoundException;
import com.wextransactionapi.models.Transaction;
import com.wextransactionapi.repositories.TransactionRepository;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TransactionService {

    @Value("${client.exchange-service.url}")
    private String exchangeServiceUrl;
    private final TransactionRepository repository;
    private final RestTemplate restTemplate = new RestTemplate();

    public TransactionResponse save(TransactionRequest request) {
        var transaction = Transaction.builder()
                .amountUSD(request.getAmount())
                .description(request.getDescription())
                .build();
        return TransactionResponse.from(repository.save(transaction));
    }

    public Transaction findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Transaction not found"));
    }

    public TransactionResponse convertTransaction(Long id, String targetCurrency) {
        var transaction = findById(id);
        var transactionDate = transaction.getCreatedAt().toLocalDate();

        LocalDate sixMonthsAgo = transactionDate.minusMonths(6);
        ResponseEntity<ExchangeRateResponse> response = getExchangeRateResponseResponse(targetCurrency, sixMonthsAgo, transactionDate);

        BigDecimal exchangeRate = Objects.requireNonNull(response.getBody()).getData().getLast().getExchangeRate();
        BigDecimal convertedAmount = transaction.getAmountUSD().multiply(exchangeRate).setScale(2, RoundingMode.HALF_UP);

        return new TransactionResponse(transaction, exchangeRate, convertedAmount, targetCurrency);
    }

    private ResponseEntity<ExchangeRateResponse> getExchangeRateResponseResponse(String targetCurrency,
                                                                                 LocalDate sixMonthsAgo, LocalDate transactionDate) {
        String url = String.format(
                exchangeServiceUrl + "filter=record_date:gte:%s,record_date:lte:%s,currency:eq:%s",
                sixMonthsAgo, transactionDate, targetCurrency
        );

        ResponseEntity<ExchangeRateResponse> response = restTemplate.getForEntity(url, ExchangeRateResponse.class);
        if (response.getBody() == null || response.getBody().getData().isEmpty()) {
            throw new IllegalArgumentException("No exchange rate found within the last 6 months");
        }
        return response;
    }
}
