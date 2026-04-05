package com.finance.dashboard.service;

import com.finance.dashboard.dto.request.CreateRecordRequest;
import com.finance.dashboard.dto.request.UpdateRecordRequest;
import com.finance.dashboard.dto.response.FinancialRecordResponse;
import com.finance.dashboard.entity.FinancialRecord.RecordType;

import java.time.LocalDate;
import java.util.List;

public interface FinancialRecordService {
    FinancialRecordResponse createRecord(CreateRecordRequest request, String creatorEmail);
    FinancialRecordResponse getRecordById(Long id);
    List<FinancialRecordResponse> getAllRecords();
    FinancialRecordResponse updateRecord(Long id, UpdateRecordRequest request);
    void deleteRecord(Long id);

    // ---- Filtering ----
    List<FinancialRecordResponse> filterRecords(RecordType type, String category,
                                                LocalDate startDate, LocalDate endDate);
}
