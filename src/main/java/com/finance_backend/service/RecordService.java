package com.finance_backend.service;

import com.finance_backend.dto.DashboardResponse;
import com.finance_backend.dto.RecordRequest;
import com.finance_backend.dto.RecordResponse;

import java.time.LocalDate;
import java.util.List;

public interface RecordService {
    RecordResponse createRecord(RecordRequest request, Long userId);
    List<RecordResponse> getRecords(Long userId, LocalDate fromDate, LocalDate toDate, String category, String type);
    RecordResponse getRecordById(Long id, Long userId);
    RecordResponse updateRecord(Long id, RecordRequest request, Long userId);
    void deleteRecord(Long id, Long userId);
    DashboardResponse getDashboard(Long userId);
}