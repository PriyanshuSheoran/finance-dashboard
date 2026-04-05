package com.finance.dashboard.service.impl;

import com.finance.dashboard.dto.response.DashboardSummaryResponse;
import com.finance.dashboard.dto.response.DashboardSummaryResponse.MonthlyTrendResponse;
import com.finance.dashboard.dto.response.FinancialRecordResponse;
import com.finance.dashboard.entity.FinancialRecord.RecordType;
import com.finance.dashboard.repository.FinancialRecordRepository;
import com.finance.dashboard.service.DashboardService;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DashboardServiceImpl implements DashboardService {

    private final FinancialRecordRepository recordRepository;

    public DashboardServiceImpl(FinancialRecordRepository recordRepository) {
        this.recordRepository = recordRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public DashboardSummaryResponse getSummary() {
        BigDecimal totalIncome   = recordRepository.sumByType(RecordType.INCOME);
        BigDecimal totalExpenses = recordRepository.sumByType(RecordType.EXPENSE);

        DashboardSummaryResponse resp = new DashboardSummaryResponse();
        resp.setTotalIncome(totalIncome);
        resp.setTotalExpenses(totalExpenses);
        resp.setNetBalance(totalIncome.subtract(totalExpenses));
        resp.setIncomeByCategory(buildCategoryMap(RecordType.INCOME));
        resp.setExpensesByCategory(buildCategoryMap(RecordType.EXPENSE));
        resp.setMonthlyTrends(buildMonthlyTrends(LocalDate.now().minusMonths(11)));
        resp.setRecentActivity(buildRecentActivity(10));
        return resp;
    }

    @Override
    @Transactional(readOnly = true)
    public DashboardSummaryResponse getSummaryForDateRange(LocalDate start, LocalDate end) {
        if (start.isAfter(end)) {
            throw new IllegalArgumentException("Start date must be before or equal to end date");
        }
        BigDecimal totalIncome   = recordRepository.sumByTypeAndDateRange(RecordType.INCOME,   start, end);
        BigDecimal totalExpenses = recordRepository.sumByTypeAndDateRange(RecordType.EXPENSE, start, end);

        DashboardSummaryResponse resp = new DashboardSummaryResponse();
        resp.setTotalIncome(totalIncome);
        resp.setTotalExpenses(totalExpenses);
        resp.setNetBalance(totalIncome.subtract(totalExpenses));
        resp.setIncomeByCategory(buildCategoryMap(RecordType.INCOME));
        resp.setExpensesByCategory(buildCategoryMap(RecordType.EXPENSE));
        resp.setMonthlyTrends(buildMonthlyTrends(start));
        resp.setRecentActivity(buildRecentActivity(5));
        return resp;
    }

    private Map<String, BigDecimal> buildCategoryMap(RecordType type) {
        Map<String, BigDecimal> result = new LinkedHashMap<>();
        for (Object[] row : recordRepository.sumByCategory(type)) {
            result.put((String) row[0], (BigDecimal) row[1]);
        }
        return result;
    }

    private List<MonthlyTrendResponse> buildMonthlyTrends(LocalDate since) {
        Map<String, MonthlyTrendResponse> map = new LinkedHashMap<>();
        for (Object[] row : recordRepository.monthlyTrends(since)) {
            int year       = ((Number) row[0]).intValue();
            int month      = ((Number) row[1]).intValue();
            RecordType type = RecordType.valueOf(row[2].toString());
            BigDecimal amt  = (BigDecimal) row[3];
            String key      = year + "-" + String.format("%02d", month);

            MonthlyTrendResponse trend = map.computeIfAbsent(key,
                    k -> new MonthlyTrendResponse(year, month, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO));

            if (type == RecordType.INCOME) {
                trend.setIncome(trend.getIncome().add(amt));
            } else {
                trend.setExpenses(trend.getExpenses().add(amt));
            }
            trend.setNet(trend.getIncome().subtract(trend.getExpenses()));
        }
        return new ArrayList<>(map.values());
    }

    private List<FinancialRecordResponse> buildRecentActivity(int limit) {
        return recordRepository.findRecentActivity(PageRequest.of(0, limit))
                .stream().map(FinancialRecordResponse::from).collect(Collectors.toList());
    }
}
