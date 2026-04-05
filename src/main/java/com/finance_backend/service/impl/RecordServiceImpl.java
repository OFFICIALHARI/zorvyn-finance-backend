package com.finance_backend.service.impl;

import com.finance_backend.dto.DashboardResponse;
import com.finance_backend.dto.RecordRequest;
import com.finance_backend.dto.RecordResponse;
import com.finance_backend.entity.FinanceRecord;
import com.finance_backend.repository.FinanceRecordRepository;
import com.finance_backend.service.RecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecordServiceImpl implements RecordService {

    private final FinanceRecordRepository repository;

    @Override
    public RecordResponse createRecord(RecordRequest request, Long userId) {

        FinanceRecord record = FinanceRecord.builder()
                .amount(request.getAmount())
                .type(request.getType())
                .category(request.getCategory())
                .userId(userId)
        .date(request.getDate() != null ? request.getDate() : LocalDate.now())
        .description(request.getDescription())
                .build();

        FinanceRecord saved = repository.save(record);

    return mapToResponse(saved);
    }

    @Override
    public List<RecordResponse> getRecords(Long userId,
                       LocalDate fromDate,
                       LocalDate toDate,
                       String category,
                       String type) {
    String normalizedType = type == null ? null : type.toUpperCase();

    return repository.searchByFilters(userId, fromDate, toDate, category, normalizedType)
        .stream()
        .map(this::mapToResponse)
        .toList();
    }

    @Override
    public RecordResponse getRecordById(Long id, Long userId) {
    FinanceRecord record = repository.findByIdAndUserId(id, userId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Record not found"));

    return mapToResponse(record);
    }

    @Override
    public RecordResponse updateRecord(Long id, RecordRequest request, Long userId) {
    FinanceRecord record = repository.findByIdAndUserId(id, userId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Record not found"));

    record.setAmount(request.getAmount());
    record.setType(request.getType());
    record.setCategory(request.getCategory());
    record.setDate(request.getDate() != null ? request.getDate() : record.getDate());
    record.setDescription(request.getDescription());

    FinanceRecord saved = repository.save(record);

    return mapToResponse(saved);
    }

    @Override
    public void deleteRecord(Long id, Long userId) {
    FinanceRecord record = repository.findByIdAndUserId(id, userId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Record not found"));

    repository.delete(record);
    }

    @Override
    public DashboardResponse getDashboard(Long userId) {
        BigDecimal income = repository.getTotalIncome(userId);
        BigDecimal expense = repository.getTotalExpense(userId);

        BigDecimal balance = income.subtract(expense);

        return new DashboardResponse(income, expense, balance);
    }

    private RecordResponse mapToResponse(FinanceRecord record) {
        return RecordResponse.builder()
                .id(record.getId())
                .amount(record.getAmount())
                .type(record.getType())
                .category(record.getCategory())
                .date(record.getDate())
                .description(record.getDescription())
                .build();
    }
}