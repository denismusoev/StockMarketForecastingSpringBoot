package com.coursework.stockmarketforecasting.controller;

import com.coursework.stockmarketforecasting.model.PredictionRequest;
import com.coursework.stockmarketforecasting.repository.ForexDataRepository;
import com.coursework.stockmarketforecasting.repository.TimeSeriesDataRepository;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.util.*;

@RestController
@RequestMapping("/api/predictions")
@CrossOrigin(origins = "http://localhost:3000")
public class PredictionController {

    @Autowired
    private ForexDataRepository forexDataRepository;

    @Autowired
    private TimeSeriesDataRepository timeSeriesRepository;

    @PostMapping
    public ResponseEntity<?> getPrediction(@RequestBody PredictionRequest request) {
        try {
            List<?> data = null;

            // В зависимости от типа данных, извлекаем нужные данные
            if ("forex".equalsIgnoreCase(request.getType())) {
                // Для Forex извлекаем данные из forexDataRepository
                data = forexDataRepository.findFirst100BySymbol(request.getFromSymbol(), request.getToSymbol());
            } else if ("stock".equalsIgnoreCase(request.getType()) || "crypto".equalsIgnoreCase(request.getType())) {
                // Для акций или криптовалюты извлекаем данные из timeSeriesRepository
                data = timeSeriesRepository.findFirst100BySymbol(request.getSymbol());
            }

            // Подготовка данных для отправки на Python сервер
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());

            // Создаем JSON-объект для отправки на Python
            Map<String, Object> payload = new HashMap<>();
            payload.put("type", request.getType());

            // Если тип "forex", то создаем строку в формате "fromSymbol/toSymbol"
            payload.put("symbol", request.getType().equalsIgnoreCase("forex") ?
                    request.getFromSymbol() + "/" + request.getToSymbol() : request.getSymbol());

            // Если тип "crypto", добавляем параметр market
            payload.put("market", request.getType().equalsIgnoreCase("crypto") ?
                    request.getMarket() : null);

            // Добавляем данные и количество дней
            payload.put("data", data);
            payload.put("dayCount", request.getDayCount());
            payload.put("model", request.getModel());

            // Преобразуем объект в JSON строку
            String jsonPayload = objectMapper.writeValueAsString(payload);

            // Отправляем данные на Python-сервер
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<>(jsonPayload, headers);

            // URL Python-сервера для предсказания
            String pythonServerUrl = "http://localhost:5000/predict";
            ResponseEntity<String> response = restTemplate.postForEntity(pythonServerUrl, entity, String.class);

            // Возврат результата клиенту
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            // В случае ошибки, возвращаем статус 500 и сообщение об ошибке
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }
}
