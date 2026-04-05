package com.finance.dashboard.repository;

import com.finance.dashboard.entity.FinancialRecord;
import com.finance.dashboard.entity.FinancialRecord.RecordType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface FinancialRecordRepository extends JpaRepository<FinancialRecord, Long>,
        JpaSpecificationExecutor<FinancialRecord> {

    // ---- Dashboard Aggregations ----

    @Query("SELECT COALESCE(SUM(r.amount), 0) FROM FinancialRecord r WHERE r.type = :type")
    BigDecimal sumByType(@Param("type") RecordType type);

    @Query("SELECT COALESCE(SUM(r.amount), 0) FROM FinancialRecord r " +
           "WHERE r.type = :type AND r.date BETWEEN :start AND :end")
    BigDecimal sumByTypeAndDateRange(
            @Param("type") RecordType type,
            @Param("start") LocalDate start,
            @Param("end") LocalDate end);

    @Query("SELECT r.category, SUM(r.amount) FROM FinancialRecord r " +
           "WHERE r.type = :type GROUP BY r.category ORDER BY SUM(r.amount) DESC")
    List<Object[]> sumByCategory(@Param("type") RecordType type);

    @Query("SELECT FUNCTION('YEAR', r.date), FUNCTION('MONTH', r.date), r.type, SUM(r.amount) " +
           "FROM FinancialRecord r " +
           "WHERE r.date >= :since " +
           "GROUP BY FUNCTION('YEAR', r.date), FUNCTION('MONTH', r.date), r.type " +
           "ORDER BY FUNCTION('YEAR', r.date) DESC, FUNCTION('MONTH', r.date) DESC")
    List<Object[]> monthlyTrends(@Param("since") LocalDate since);

    @Query("SELECT r FROM FinancialRecord r ORDER BY r.createdAt DESC")
    List<FinancialRecord> findRecentActivity(org.springframework.data.domain.Pageable pageable);

    // ---- Filtering ----
    List<FinancialRecord> findByType(RecordType type);
    List<FinancialRecord> findByCategory(String category);
    List<FinancialRecord> findByDateBetween(LocalDate start, LocalDate end);
    List<FinancialRecord> findByTypeAndDateBetween(RecordType type, LocalDate start, LocalDate end);
    List<FinancialRecord> findByCategoryAndDateBetween(String category, LocalDate start, LocalDate end);
}
