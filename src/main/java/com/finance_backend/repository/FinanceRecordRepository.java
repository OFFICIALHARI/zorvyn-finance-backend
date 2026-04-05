package com.finance_backend.repository;

import com.finance_backend.entity.FinanceRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface FinanceRecordRepository extends JpaRepository<FinanceRecord, Long> {

    List<FinanceRecord> findByUserId(Long userId);

        Optional<FinanceRecord> findByIdAndUserId(Long id, Long userId);

        @Query("""
                        SELECT r FROM FinanceRecord r
                        WHERE r.userId = :userId
                            AND (:fromDate IS NULL OR r.date >= :fromDate)
                            AND (:toDate IS NULL OR r.date <= :toDate)
                            AND (:category IS NULL OR LOWER(r.category) = LOWER(:category))
                            AND (:type IS NULL OR r.type = :type)
                        ORDER BY r.date DESC, r.id DESC
                        """)
        List<FinanceRecord> searchByFilters(@Param("userId") Long userId,
                                                                                @Param("fromDate") LocalDate fromDate,
                                                                                @Param("toDate") LocalDate toDate,
                                                                                @Param("category") String category,
                                                                                @Param("type") String type);

    @Query("SELECT COALESCE(SUM(r.amount), 0) FROM FinanceRecord r WHERE r.userId = :userId AND r.type = 'INCOME'")
    BigDecimal getTotalIncome(@Param("userId") Long userId);

    @Query("SELECT COALESCE(SUM(r.amount), 0) FROM FinanceRecord r WHERE r.userId = :userId AND r.type = 'EXPENSE'")
    BigDecimal getTotalExpense(@Param("userId") Long userId);
}
