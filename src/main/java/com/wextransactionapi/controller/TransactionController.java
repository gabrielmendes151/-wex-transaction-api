package com.wextransactionapi.controller;

import com.wextransactionapi.dto.TransactionRequest;
import com.wextransactionapi.dto.TransactionResponse;
import com.wextransactionapi.services.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Operation(summary = "Create a new transaction", description = "Saves a new transaction with the details provided.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Transaction created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TransactionResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content)
    })
    public ResponseEntity<TransactionResponse> save(
            @RequestBody @Valid @Parameter(description = "Transaction details to be saved", required = true)
            TransactionRequest request) {
        var response = service.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}/convert")
    @Operation(summary = "Convert transaction amount", description = "Converts the transaction amount to the specified target currency.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transaction converted successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TransactionResponse.class))),
            @ApiResponse(responseCode = "404", description = "Transaction not found", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid target currency", content = @Content)
    })
    public ResponseEntity<TransactionResponse> convertTransaction(
            @PathVariable @Parameter(description = "ID of the transaction to convert", required = true) Long id,
            @RequestParam @Parameter(description = "Target currency for conversion (e.g., Dollar)", required = true) String targetCurrency) {
        TransactionResponse result = service.convertTransaction(id, targetCurrency);
        return ResponseEntity.ok(result);
    }
}
