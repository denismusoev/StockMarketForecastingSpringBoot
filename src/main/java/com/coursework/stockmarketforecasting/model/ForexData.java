package com.coursework.stockmarketforecasting.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;


@Entity
@Table(name = "forex_data")
@Data
public class ForexData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Тип данных (DAILY, WEEKLY, MONTHLY)
    @Enumerated(EnumType.STRING)
    @Column(name = "data_type", nullable = false)
    private DataType dataType;

    // Символ исходной валюты (например, EUR)
    @Column(name = "from_symbol", nullable = false)
    private String fromSymbol;

    // Символ целевой валюты (например, USD)
    @Column(name = "to_symbol", nullable = false)
    private String toSymbol;

    // Дата записи
    @Column(name = "record_date", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd") // Формат ISO-8601
    private LocalDate recordDate;

    // Цены
    @Column(name = "open_price", nullable = false)
    private Double openPrice;

    @Column(name = "high_price", nullable = false)
    private Double highPrice;

    @Column(name = "low_price", nullable = false)
    private Double lowPrice;

    @Column(name = "close_price", nullable = false)
    private Double closePrice;
}
