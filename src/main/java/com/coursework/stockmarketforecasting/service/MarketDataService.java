package com.coursework.stockmarketforecasting.service;

import com.coursework.stockmarketforecasting.model.DataType;
import com.coursework.stockmarketforecasting.model.ForexData;
import com.coursework.stockmarketforecasting.model.TimeSeriesData;
import com.coursework.stockmarketforecasting.repository.ForexDataRepository;
import com.coursework.stockmarketforecasting.repository.TimeSeriesDataRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.Map;

@Service
public class MarketDataService {

    private final AlphaVantageClient apiClient;
    private final TimeSeriesDataRepository timeSeriesRepository;
    private final ForexDataRepository forexDataRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public MarketDataService(AlphaVantageClient apiClient,
                             TimeSeriesDataRepository timeSeriesRepository, ForexDataRepository forexDataRepository) {
        this.apiClient = apiClient;
        this.timeSeriesRepository = timeSeriesRepository;
        this.forexDataRepository = forexDataRepository;
    }

    // Метод для акций
    public void fetchAndSaveStockData(String function, String symbol, String outputSize) {
        String response = apiClient.fetchData(function, symbol, null, outputSize);
        processStockData(response, symbol, "daily");
    }

    // Метод для криптовалют
    public void fetchAndSaveCryptoData(String function, String symbol, String market, String outputSize) {
        String response = apiClient.fetchData(function, symbol, market, outputSize);
        processCryptoData(response, symbol, market, "digital_daily");
    }

    // Метод для Forex временных рядов
    public void fetchAndSaveForexData(String function, String fromSymbol, String toSymbol, String outputSize) {
        String response = apiClient.fetchForex(function, fromSymbol, toSymbol, outputSize);
        processForexData(response, fromSymbol, toSymbol, "forex_daily"); // Указываем тип данных, например, DAILY
    }

    // Обработка данных акций
    private void processStockData(String response, String symbol, String type) {
        try {
            JsonNode root = objectMapper.readTree(response);
            JsonNode timeSeries = findTimeSeriesNode(root, type);
            if (timeSeries == null) {
                throw new IllegalArgumentException("Stock data not found for type: " + type);
            }

            Iterator<String> dates = timeSeries.fieldNames();
            while (dates.hasNext()) {
                String date = dates.next();
                JsonNode data = timeSeries.get(date);

                // Создание объекта TimeSeriesData для акций
                TimeSeriesData timeSeriesData = new TimeSeriesData();
                timeSeriesData.setSymbol(symbol);
                timeSeriesData.setRecordDate(java.time.LocalDate.parse(date));
                timeSeriesData.setDataType(DataType.valueOf(type.toUpperCase())); // Устанавливаем тип данных (DAILY, WEEKLY, MONTHLY)
                timeSeriesData.setOpenPrice(data.path("1. open").asDouble());
                timeSeriesData.setHighPrice(data.path("2. high").asDouble());
                timeSeriesData.setLowPrice(data.path("3. low").asDouble());
                timeSeriesData.setClosePrice(data.path("4. close").asDouble());
                timeSeriesData.setVolume(data.path("5. volume").asDouble());
                timeSeriesData.setMarket(null); // Для акций указываем как "Stock Market"

                timeSeriesRepository.save(timeSeriesData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void processCryptoData(String response, String symbol, String market, String type) {
        try {
            JsonNode root = objectMapper.readTree(response);
            JsonNode timeSeries = findTimeSeriesNode(root, type);
            if (timeSeries == null) {
                throw new IllegalArgumentException("Crypto data not found for type: " + type);
            }

            Iterator<String> dates = timeSeries.fieldNames();
            while (dates.hasNext()) {
                String date = dates.next();
                JsonNode data = timeSeries.get(date);

                // Создание объекта TimeSeriesData для криптовалюты
                TimeSeriesData timeSeriesData = new TimeSeriesData();
                timeSeriesData.setSymbol(symbol);
                timeSeriesData.setRecordDate(java.time.LocalDate.parse(date));
                timeSeriesData.setDataType(DataType.valueOf(type.toUpperCase())); // Устанавливаем тип данных
                timeSeriesData.setOpenPrice(data.path("1. open").asDouble());
                timeSeriesData.setHighPrice(data.path("2. high").asDouble());
                timeSeriesData.setLowPrice(data.path("3. low").asDouble());
                timeSeriesData.setClosePrice(data.path("4. close").asDouble());
                timeSeriesData.setVolume(data.path("5. volume").asDouble());
                timeSeriesData.setMarket(market); // Для криптовалют указываем как "Crypto Market"

                timeSeriesRepository.save(timeSeriesData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void processForexData(String response, String fromSymbol, String toSymbol, String type) {
        try {
            JsonNode root = objectMapper.readTree(response);
            JsonNode timeSeries = findTimeSeriesNode(root, type);
            if (timeSeries == null) {
                throw new IllegalArgumentException("Forex data not found for type: " + type);
            }

            Iterator<String> dates = timeSeries.fieldNames();
            while (dates.hasNext()) {
                String date = dates.next();
                JsonNode data = timeSeries.get(date);

                // Создание объекта ForexData
                ForexData forexData = new ForexData();
                forexData.setFromSymbol(fromSymbol);
                forexData.setToSymbol(toSymbol);
                forexData.setRecordDate(java.time.LocalDate.parse(date));
                forexData.setDataType(DataType.valueOf(type.toUpperCase())); // Устанавливаем тип данных
                forexData.setOpenPrice(data.path("1. open").asDouble());
                forexData.setHighPrice(data.path("2. high").asDouble());
                forexData.setLowPrice(data.path("3. low").asDouble());
                forexData.setClosePrice(data.path("4. close").asDouble());

                // Сохраняем данные в базу
                forexDataRepository.save(forexData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private JsonNode findTimeSeriesNode(JsonNode root, String type) {
        Map<String, String> nodeNames = Map.of(
                "daily", "Time Series (Daily)",
                "weekly", "Time Series (Weekly)",
                "monthly", "Time Series (Monthly)",
                "digital_daily", "Time Series (Digital Currency Daily)",
                "digital_weekly", "Time Series (Digital Currency Weekly)",
                "digital_monthly", "Time Series (Digital Currency Monthly)",
                "forex_daily", "Time Series FX (Daily)",
                "forex_weekly", "Time Series FX (Weekly)",
                "forex_monthly", "Time Series FX (Monthly)"
        );

        String nodeName = nodeNames.getOrDefault(type.toLowerCase(), null);
        if (nodeName != null && root.has(nodeName)) {
            return root.path(nodeName);
        }
        return null;
    }
}
