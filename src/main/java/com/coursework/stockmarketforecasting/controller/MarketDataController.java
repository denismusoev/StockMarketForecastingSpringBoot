package com.coursework.stockmarketforecasting.controller;

import com.coursework.stockmarketforecasting.service.MarketDataService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/marketdata")
public class MarketDataController {

    private final MarketDataService service;

    public MarketDataController(MarketDataService service) {
        this.service = service;
    }

    // Эндпоинт для загрузки данных акций
    @PostMapping("/stocks")
    public String fetchStockData(@RequestParam String function,
                                 @RequestParam String symbol,
                                 @RequestParam(required = false) String outputSize) {
        service.fetchAndSaveStockData(function, symbol, outputSize);
        return "Stock data fetched and saved successfully.";
    }

    // Эндпоинт для загрузки данных криптовалют
    @PostMapping("/crypto")
    public String fetchCryptoData(@RequestParam String function,
                                  @RequestParam String symbol,
                                  @RequestParam String market,
                                  @RequestParam(required = false) String outputSize) {
        service.fetchAndSaveCryptoData(function, symbol, market, outputSize);
        return "Crypto data fetched and saved successfully.";
    }

    // Эндпоинт для загрузки данных Forex
    @PostMapping("/forex")
    public String fetchForexData(@RequestParam String function,
                                 @RequestParam String fromSymbol,
                                 @RequestParam String toSymbol,
                                 @RequestParam(required = false) String outputSize) {
        service.fetchAndSaveForexData(function, fromSymbol, toSymbol, outputSize);
        return "Forex data fetched and saved successfully.";
    }
}
