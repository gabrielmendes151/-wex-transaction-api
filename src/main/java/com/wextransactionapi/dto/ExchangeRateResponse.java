package com.wextransactionapi.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExchangeRateResponse {

    private List<ExchangeRateData> data;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ExchangeRateData {
        @JsonProperty("record_date")
        private String recordDate;

        @JsonProperty("currency")
        private String currency;

        @JsonProperty("exchange_rate")
        private BigDecimal exchangeRate;
    }
}
