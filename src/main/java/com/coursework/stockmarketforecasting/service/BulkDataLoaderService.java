package com.coursework.stockmarketforecasting.service;

import org.springframework.stereotype.Service;

@Service
public class BulkDataLoaderService {

    private final MarketDataService marketDataService;

    public BulkDataLoaderService(MarketDataService marketDataService) {
        this.marketDataService = marketDataService;
    }

    // Загрузка данных акций
    public void loadStockData(String symbol) {
        marketDataService.fetchAndSaveStockData("TIME_SERIES_DAILY", symbol, "compact");
    }

    // Загрузка данных криптовалют
    public void loadCryptoData(String symbol, String market) {
        marketDataService.fetchAndSaveCryptoData("DIGITAL_CURRENCY_DAILY", symbol, market, "compact");
    }

    // Загрузка данных валютных пар
    public void loadForexData(String fromSymbol, String toSymbol) {
        marketDataService.fetchAndSaveForexData("FX_DAILY", fromSymbol, toSymbol, "compact");
    }

    public void loadAllData() {
        // Загрузка данных временных рядов для акций
        loadStockTimeSeries();

        // Загрузка данных временных рядов для криптовалют
        loadCryptoTimeSeries();

        // Загрузка данных для Forex

        loadForexTimeSeries();
    }

    // Загрузка данных акций (ежедневные, еженедельные и ежемесячные)
    private void loadStockTimeSeries() {
        String[] symbols = {"AAPL", "IBM", "GOOGL", "MSFT"}; // Пример тикеров

        for (String symbol : symbols) {
            marketDataService.fetchAndSaveStockData("TIME_SERIES_DAILY", symbol, "compact");
//            marketDataService.fetchAndSaveStockData("TIME_SERIES_WEEKLY", symbol, "compact");
//            marketDataService.fetchAndSaveStockData("TIME_SERIES_MONTHLY", symbol, "compact");
        }
    }

    // Загрузка данных криптовалют (ежедневные, еженедельные и ежемесячные)
    private void loadCryptoTimeSeries() {
        String[] cryptoSymbols = {"BTC", "ETH", "XRP", "LTC"}; // Пример криптовалют
        String market = "USD";

        for (String symbol : cryptoSymbols) {
            marketDataService.fetchAndSaveCryptoData("DIGITAL_CURRENCY_DAILY", symbol, market, "compact");
//            marketDataService.fetchAndSaveCryptoData("DIGITAL_CURRENCY_WEEKLY", symbol, market, "compact");
//            marketDataService.fetchAndSaveCryptoData("DIGITAL_CURRENCY_MONTHLY", symbol, market, "compact");
        }
    }

    // Загрузка данных Forex (ежедневные, еженедельные и ежемесячные)
    private void loadForexTimeSeries() {
        String[][] forexPairs = {
                {"EUR", "USD"},
                {"GBP", "USD"},
                {"AUD", "USD"},
                {"JPY", "USD"}
        };

        for (String[] pair : forexPairs) {
            marketDataService.fetchAndSaveForexData("FX_DAILY", pair[0], pair[1], "compact");
//            marketDataService.fetchAndSaveForexData("FX_WEEKLY", pair[0], pair[1], "compact");
//            marketDataService.fetchAndSaveForexData("FX_MONTHLY", pair[0], pair[1], "compact");
        }
    }
}
