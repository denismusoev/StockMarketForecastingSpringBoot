package com.coursework.stockmarketforecasting.repository;

import com.coursework.stockmarketforecasting.model.ForexData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ForexDataRepository extends JpaRepository<ForexData, Long> {
    @Query(value = "SELECT * FROM forex_data WHERE from_symbol = ?1 AND to_symbol = ?2 ORDER BY record_date ASC LIMIT 365", nativeQuery = true)
    List<ForexData> findFirst100BySymbol(String fromSymbol, String toSymbol);
}
