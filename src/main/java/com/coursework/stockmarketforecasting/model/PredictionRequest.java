package com.coursework.stockmarketforecasting.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PredictionRequest {
    private String type; // Тип данных: "stock", "crypto", или "forex"
    private String symbol; // Символ акции или криптовалюты
    private String market;
    private String fromSymbol; // Исходная валюта (для Forex)
    private String toSymbol; // Целевая валюта (для Forex)
    private int dayCount; // Количество прогнозов
    private String model;
}

