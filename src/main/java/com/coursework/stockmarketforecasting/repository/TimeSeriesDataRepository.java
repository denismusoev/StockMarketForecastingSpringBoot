package com.coursework.stockmarketforecasting.repository;

import com.coursework.stockmarketforecasting.model.TimeSeriesData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TimeSeriesDataRepository extends JpaRepository<TimeSeriesData, Long> {
    @Query(value = "SELECT * FROM time_series_data WHERE symbol = ?1 ORDER BY record_date DESC LIMIT 365", nativeQuery = true)
    List<TimeSeriesData> findFirst100BySymbol(String symbol);
}


