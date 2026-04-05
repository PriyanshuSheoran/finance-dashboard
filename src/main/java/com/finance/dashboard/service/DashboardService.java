package com.finance.dashboard.service;

import com.finance.dashboard.dto.response.DashboardSummaryResponse;

public interface DashboardService {
    DashboardSummaryResponse getSummary();
    DashboardSummaryResponse getSummaryForDateRange(java.time.LocalDate start, java.time.LocalDate end);
}
