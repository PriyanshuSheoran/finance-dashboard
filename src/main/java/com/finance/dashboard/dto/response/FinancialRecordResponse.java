package com.finance.dashboard.dto.response;

import com.finance.dashboard.entity.FinancialRecord;
import com.finance.dashboard.entity.FinancialRecord.RecordType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class FinancialRecordResponse {

    private Long id;
    private BigDecimal amount;
    private RecordType type;
    private String category;
    private LocalDate date;
    private String notes;
    private String createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public FinancialRecordResponse() {}

    public static FinancialRecordResponse from(FinancialRecord r) {
        FinancialRecordResponse dto = new FinancialRecordResponse();
        dto.id        = r.getId();
        dto.amount    = r.getAmount();
        dto.type      = r.getType();
        dto.category  = r.getCategory();
        dto.date      = r.getDate();
        dto.notes     = r.getNotes();
        dto.createdBy = r.getCreatedBy().getFullName();
        dto.createdAt = r.getCreatedAt();
        dto.updatedAt = r.getUpdatedAt();
        return dto;
    }

    public Long getId()                  { return id; }
    public BigDecimal getAmount()        { return amount; }
    public RecordType getType()          { return type; }
    public String getCategory()          { return category; }
    public LocalDate getDate()           { return date; }
    public String getNotes()             { return notes; }
    public String getCreatedBy()         { return createdBy; }
    public LocalDateTime getCreatedAt()  { return createdAt; }
    public LocalDateTime getUpdatedAt()  { return updatedAt; }

    public void setId(Long v)                  { this.id = v; }
    public void setAmount(BigDecimal v)        { this.amount = v; }
    public void setType(RecordType v)          { this.type = v; }
    public void setCategory(String v)          { this.category = v; }
    public void setDate(LocalDate v)           { this.date = v; }
    public void setNotes(String v)             { this.notes = v; }
    public void setCreatedBy(String v)         { this.createdBy = v; }
    public void setCreatedAt(LocalDateTime v)  { this.createdAt = v; }
    public void setUpdatedAt(LocalDateTime v)  { this.updatedAt = v; }
}
