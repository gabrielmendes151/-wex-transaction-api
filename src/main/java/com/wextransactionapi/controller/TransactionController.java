package com.wextransactionapi.controller;

import com.wextransactionapi.dto.TransactionRequest;
import com.wextransactionapi.dto.TransactionResponse;
import com.wextransactionapi.models.Transaction;
import com.wextransactionapi.services.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService service;

    @PostMapping
    public ResponseEntity<TransactionResponse> save(@RequestBody @Valid TransactionRequest request) {
        var response = service.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}/convert")
    public ResponseEntity<TransactionResponse> convertTransaction(@PathVariable Long id, @RequestParam String targetCurrency) {
        TransactionResponse result = service.convertTransaction(id, targetCurrency);
        return ResponseEntity.ok(result);
    }
}
