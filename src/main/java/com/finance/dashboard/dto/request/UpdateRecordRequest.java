package com.finance.dashboard.dto.request;

import com.finance.dashboard.entity.FinancialRecord.RecordType;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public class UpdateRecordRequest {

    @DecimalMin(value = "0.01")
    @Digits(integer = 13, fraction = 2)
    private BigDecimal amount;

    private RecordType type;

    @Size(max = 100)
    private String category;

    @PastOrPresent
    private LocalDate date;

    @Size(max = 500)
    private String notes;

    public BigDecimal getAmount()          { return amount; }
    public void setAmount(BigDecimal v)    { this.amount = v; }
    public RecordType getType()            { return type; }
    public void setType(RecordType v)      { this.type = v; }
    public String getCategory()            { return category; }
    public void setCategory(String v)      { this.category = v; }
    public LocalDate getDate()             { return date; }
    public void setDate(LocalDate v)       { this.date = v; }
    public String getNotes()               { return notes; }
    public void setNotes(String v)         { this.notes = v; }
}
