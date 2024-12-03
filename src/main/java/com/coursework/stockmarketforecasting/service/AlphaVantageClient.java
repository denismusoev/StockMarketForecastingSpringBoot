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

    public String fetchForex(String function, String from_symbol, String to_symbol, String outputSize) {
        String baseUrl = "https://www.alphavantage.co/query?";
        String url = baseUrl + "function=" + function +
                "&from_symbol=" + from_symbol +
                "&to_symbol=" + to_symbol +
                "&outputsize=" + (outputSize != null ? outputSize : "compact") +
                "&apikey=" + apiKey;

        return restTemplate.getForObject(url, String.class);
    }
}

