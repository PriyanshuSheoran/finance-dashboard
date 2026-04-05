package com.finance.dashboard.dto.response;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class DashboardSummaryResponse {

    private BigDecimal totalIncome;
    private BigDecimal totalExpenses;
    private BigDecimal netBalance;
    private Map<String, BigDecimal> incomeByCategory;
    private Map<String, BigDecimal> expensesByCategory;
    private List<MonthlyTrendResponse> monthlyTrends;
    private List<FinancialRecordResponse> recentActivity;

    public DashboardSummaryResponse() {}

    // Getters
    public BigDecimal getTotalIncome()                          { return totalIncome; }
    public BigDecimal getTotalExpenses()                        { return totalExpenses; }
    public BigDecimal getNetBalance()                           { return netBalance; }
    public Map<String, BigDecimal> getIncomeByCategory()        { return incomeByCategory; }
    public Map<String, BigDecimal> getExpensesByCategory()      { return expensesByCategory; }
    public List<MonthlyTrendResponse> getMonthlyTrends()        { return monthlyTrends; }
    public List<FinancialRecordResponse> getRecentActivity()    { return recentActivity; }

    // Setters
    public void setTotalIncome(BigDecimal v)                          { this.totalIncome = v; }
    public void setTotalExpenses(BigDecimal v)                        { this.totalExpenses = v; }
    public void setNetBalance(BigDecimal v)                           { this.netBalance = v; }
    public void setIncomeByCategory(Map<String, BigDecimal> v)        { this.incomeByCategory = v; }
    public void setExpensesByCategory(Map<String, BigDecimal> v)      { this.expensesByCategory = v; }
    public void setMonthlyTrends(List<MonthlyTrendResponse> v)        { this.monthlyTrends = v; }
    public void setRecentActivity(List<FinancialRecordResponse> v)    { this.recentActivity = v; }

    // ---- Inner class ----
    public static class MonthlyTrendResponse {
        private int year;
        private int month;
        private BigDecimal income;
        private BigDecimal expenses;
        private BigDecimal net;

        public MonthlyTrendResponse() {}

        public MonthlyTrendResponse(int year, int month,
                                     BigDecimal income, BigDecimal expenses, BigDecimal net) {
            this.year     = year;
            this.month    = month;
            this.income   = income;
            this.expenses = expenses;
            this.net      = net;
        }

        public int getYear()               { return year; }
        public int getMonth()              { return month; }
        public BigDecimal getIncome()      { return income; }
        public BigDecimal getExpenses()    { return expenses; }
        public BigDecimal getNet()         { return net; }

        public void setYear(int v)              { this.year = v; }
        public void setMonth(int v)             { this.month = v; }
        public void setIncome(BigDecimal v)     { this.income = v; }
        public void setExpenses(BigDecimal v)   { this.expenses = v; }
        public void setNet(BigDecimal v)        { this.net = v; }
    }
}
