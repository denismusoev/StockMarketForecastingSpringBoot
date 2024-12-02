package com.coursework.stockmarketforecasting.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "time_series_data")
@Data
public class TimeSeriesData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Тип данных: DAILY, WEEKLY, MONTHLY
    @Enumerated(EnumType.STRING)
    @Column(name = "data_type", nullable = false)
    private DataType dataType;

    // Символ акции, криптовалюты или Forex
    @Column(name = "symbol", nullable = false)
    private String symbol;

    // Для криптовалют (например, BTC/EUR)
    @Column(name = "market")
    private String market;

    // Дата записи
    @Column(name = "record_date", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd") // Формат ISO-8601
    private LocalDate recordDate;

    // Цены и объемы
    @Column(name = "open_price", nullable = false)
    private Double openPrice;

    @Column(name = "high_price", nullable = false)
    private Double highPrice;

    @Column(name = "low_price", nullable = false)
    private Double lowPrice;

    @Column(name = "close_price", nullable = false)
    private Double closePrice;

    @Column(name = "volume", nullable = true)
    private Double volume;
}

