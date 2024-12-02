package com.coursework.stockmarketforecasting.service;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class AlphaVantageClient {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String apiKey = "JYEE7SJEEKFPE1I1";

    public String fetchData(String function, String symbol, String market, String outputSize) {
        String baseUrl = "https://www.alphavantage.co/query?";
        String url = baseUrl + "function=" + function +
                "&symbol=" + symbol +
                (market != null ? "&market=" + market : "") +
                "&outputsize=" + (outputSize != null ? outputSize : "compact") +
                "&apikey=" + apiKey;

        return restTemplate.getForObject(url, String.class);
    }

    public String fetchCurrencyExchangeRate(String fromCurrency, String toCurrency) {
        String url = "https://www.alphavantage.co/query?function=CURRENCY_EXCHANGE_RATE" +
                "&from_currency=" + fromCurrency +
                "&to_currency=" + toCurrency +
                "&apikey=" + apiKey;

        return restTemplate.getForObject(url, String.class);
    }
}

