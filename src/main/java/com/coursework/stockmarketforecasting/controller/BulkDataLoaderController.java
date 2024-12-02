package com.coursework.stockmarketforecasting.controller;

import com.coursework.stockmarketforecasting.service.BulkDataLoaderService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bulk")
public class BulkDataLoaderController {

    private final BulkDataLoaderService bulkDataLoaderService;

    public BulkDataLoaderController(BulkDataLoaderService bulkDataLoaderService) {
        this.bulkDataLoaderService = bulkDataLoaderService;
    }

    @PostMapping("/load")
    public String loadAllData() {
        bulkDataLoaderService.loadAllData();
        return "All data types have been fetched and saved successfully.";
    }

    // Метод для загрузки данных акций
    @PostMapping("/loadStock")
    public String loadStockData(@RequestParam String symbol) {
        bulkDataLoaderService.loadStockData(symbol);
        return "Stock data for " + symbol + " has been fetched and saved successfully.";
    }

    // Метод для загрузки данных криптовалют
    @PostMapping("/loadCrypto")
    public String loadCryptoData(@RequestParam String symbol, @RequestParam String market) {
        bulkDataLoaderService.loadCryptoData(symbol, market);
        return "Crypto data for " + symbol + " in market " + market + " has been fetched and saved successfully.";
    }

    // Метод для загрузки данных валютных пар
    @PostMapping("/loadForex")
    public String loadForexData(@RequestParam String fromSymbol, @RequestParam String toSymbol) {
        bulkDataLoaderService.loadForexData(fromSymbol, toSymbol);
        return "Forex data for " + fromSymbol + "/" + toSymbol + " has been fetched and saved successfully.";
    }
}

