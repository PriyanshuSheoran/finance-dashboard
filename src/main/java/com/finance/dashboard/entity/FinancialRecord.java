package com.finance.dashboard.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "financial_records")
public class FinancialRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @DecimalMin(value = "0.01")
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RecordType type;

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false)
    private String category;

    @NotNull
    @Column(nullable = false)
    private LocalDate date;

    @Size(max = 500)
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @Column(updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt;

    public FinancialRecord() {}

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public enum RecordType { INCOME, EXPENSE }

    // Getters
    public Long getId()                  { return id; }
    public BigDecimal getAmount()        { return amount; }
    public RecordType getType()          { return type; }
    public String getCategory()          { return category; }
    public LocalDate getDate()           { return date; }
    public String getNotes()             { return notes; }
    public User getCreatedBy()           { return createdBy; }
    public LocalDateTime getCreatedAt()  { return createdAt; }
    public LocalDateTime getUpdatedAt()  { return updatedAt; }

    // Setters
    public void setId(Long id)                   { this.id = id; }
    public void setAmount(BigDecimal amount)      { this.amount = amount; }
    public void setType(RecordType type)          { this.type = type; }
    public void setCategory(String category)      { this.category = category; }
    public void setDate(LocalDate date)           { this.date = date; }
    public void setNotes(String notes)            { this.notes = notes; }
    public void setCreatedBy(User createdBy)      { this.createdBy = createdBy; }
    public void setCreatedAt(LocalDateTime v)     { this.createdAt = v; }
    public void setUpdatedAt(LocalDateTime v)     { this.updatedAt = v; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private final FinancialRecord r = new FinancialRecord();
        public Builder amount(BigDecimal v)    { r.amount = v;    return this; }
        public Builder type(RecordType v)      { r.type = v;      return this; }
        public Builder category(String v)      { r.category = v;  return this; }
        public Builder date(LocalDate v)       { r.date = v;      return this; }
        public Builder notes(String v)         { r.notes = v;     return this; }
        public Builder createdBy(User v)       { r.createdBy = v; return this; }
        public FinancialRecord build()         { return r; }
    }
}
