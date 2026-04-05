package com.finance.dashboard.service.impl;

import com.finance.dashboard.dto.request.CreateRecordRequest;
import com.finance.dashboard.dto.request.UpdateRecordRequest;
import com.finance.dashboard.dto.response.FinancialRecordResponse;
import com.finance.dashboard.entity.FinancialRecord;
import com.finance.dashboard.entity.FinancialRecord.RecordType;
import com.finance.dashboard.entity.User;
import com.finance.dashboard.exception.ResourceNotFoundException;
import com.finance.dashboard.repository.FinancialRecordRepository;
import com.finance.dashboard.repository.UserRepository;
import com.finance.dashboard.service.FinancialRecordService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FinancialRecordServiceImpl implements FinancialRecordService {

    private final FinancialRecordRepository recordRepository;
    private final UserRepository userRepository;

    public FinancialRecordServiceImpl(FinancialRecordRepository recordRepository,
                                       UserRepository userRepository) {
        this.recordRepository = recordRepository;
        this.userRepository   = userRepository;
    }

    @Override
    @Transactional
    public FinancialRecordResponse createRecord(CreateRecordRequest req, String creatorEmail) {
        User creator = userRepository.findByEmail(creatorEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + creatorEmail));

        FinancialRecord record = FinancialRecord.builder()
                .amount(req.getAmount()).type(req.getType())
                .category(req.getCategory().trim()).date(req.getDate())
                .notes(req.getNotes()).createdBy(creator).build();

        return FinancialRecordResponse.from(recordRepository.save(record));
    }

    @Override
    @Transactional(readOnly = true)
    public FinancialRecordResponse getRecordById(Long id) {
        return FinancialRecordResponse.from(findOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<FinancialRecordResponse> getAllRecords() {
        return recordRepository.findAll().stream()
                .map(FinancialRecordResponse::from).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public FinancialRecordResponse updateRecord(Long id, UpdateRecordRequest req) {
        FinancialRecord record = findOrThrow(id);
        if (req.getAmount()   != null) record.setAmount(req.getAmount());
        if (req.getType()     != null) record.setType(req.getType());
        if (req.getCategory() != null) record.setCategory(req.getCategory().trim());
        if (req.getDate()     != null) record.setDate(req.getDate());
        if (req.getNotes()    != null) record.setNotes(req.getNotes());
        return FinancialRecordResponse.from(recordRepository.save(record));
    }

    @Override
    @Transactional
    public void deleteRecord(Long id) {
        if (!recordRepository.existsById(id)) throw new ResourceNotFoundException("FinancialRecord", id);
        recordRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FinancialRecordResponse> filterRecords(RecordType type, String category,
                                                        LocalDate startDate, LocalDate endDate) {
        List<FinancialRecord> results;
        boolean hasType      = type != null;
        boolean hasCategory  = category != null && !category.isBlank();
        boolean hasDateRange = startDate != null && endDate != null;

        if (hasType && hasDateRange)         results = recordRepository.findByTypeAndDateBetween(type, startDate, endDate);
        else if (hasCategory && hasDateRange) results = recordRepository.findByCategoryAndDateBetween(category, startDate, endDate);
        else if (hasType)                     results = recordRepository.findByType(type);
        else if (hasCategory)                 results = recordRepository.findByCategory(category);
        else if (hasDateRange)                results = recordRepository.findByDateBetween(startDate, endDate);
        else                                  results = recordRepository.findAll();

        return results.stream().map(FinancialRecordResponse::from).collect(Collectors.toList());
    }

    private FinancialRecord findOrThrow(Long id) {
        return recordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("FinancialRecord", id));
    }
}
