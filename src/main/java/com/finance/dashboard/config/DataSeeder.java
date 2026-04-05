package com.finance.dashboard.config;

import com.finance.dashboard.entity.FinancialRecord;
import com.finance.dashboard.entity.FinancialRecord.RecordType;
import com.finance.dashboard.entity.Role;
import com.finance.dashboard.entity.User;
import com.finance.dashboard.repository.FinancialRecordRepository;
import com.finance.dashboard.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);

    private final UserRepository userRepository;
    private final FinancialRecordRepository recordRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(UserRepository userRepository,
                      FinancialRecordRepository recordRepository,
                      PasswordEncoder passwordEncoder) {
        this.userRepository   = userRepository;
        this.recordRepository = recordRepository;
        this.passwordEncoder  = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() > 0) {
            log.info("Database already seeded — skipping.");
            return;
        }

        User admin = userRepository.save(User.builder()
                .fullName("Admin User").email("admin@finance.com")
                .password(passwordEncoder.encode("admin123"))
                .role(Role.ADMIN).active(true).build());

        userRepository.save(User.builder()
                .fullName("Ana Analyst").email("analyst@finance.com")
                .password(passwordEncoder.encode("analyst123"))
                .role(Role.ANALYST).active(true).build());

        userRepository.save(User.builder()
                .fullName("Victor Viewer").email("viewer@finance.com")
                .password(passwordEncoder.encode("viewer123"))
                .role(Role.VIEWER).active(true).build());

        log.info("Created users: admin / analyst / viewer @finance.com");

        List<FinancialRecord> records = List.of(
            rec(new BigDecimal("5000.00"), RecordType.INCOME,  "Salary",      LocalDate.now().minusMonths(2).withDayOfMonth(1),  "Monthly salary",   admin),
            rec(new BigDecimal("5000.00"), RecordType.INCOME,  "Salary",      LocalDate.now().minusMonths(1).withDayOfMonth(1),  "Monthly salary",   admin),
            rec(new BigDecimal("5000.00"), RecordType.INCOME,  "Salary",      LocalDate.now().withDayOfMonth(1),                 "Monthly salary",   admin),
            rec(new BigDecimal("1200.00"), RecordType.INCOME,  "Freelance",   LocalDate.now().minusMonths(2).withDayOfMonth(15), "Design project",   admin),
            rec(new BigDecimal("300.00"),  RecordType.INCOME,  "Investments", LocalDate.now().minusMonths(1).withDayOfMonth(28), "Dividend",         admin),
            rec(new BigDecimal("1500.00"), RecordType.EXPENSE, "Rent",        LocalDate.now().minusMonths(2).withDayOfMonth(1),  "Monthly rent",     admin),
            rec(new BigDecimal("1500.00"), RecordType.EXPENSE, "Rent",        LocalDate.now().minusMonths(1).withDayOfMonth(1),  "Monthly rent",     admin),
            rec(new BigDecimal("1500.00"), RecordType.EXPENSE, "Rent",        LocalDate.now().withDayOfMonth(1),                 "Monthly rent",     admin),
            rec(new BigDecimal("320.00"),  RecordType.EXPENSE, "Groceries",   LocalDate.now().minusMonths(1).withDayOfMonth(10), "Groceries",        admin),
            rec(new BigDecimal("85.00"),   RecordType.EXPENSE, "Utilities",   LocalDate.now().minusMonths(1).withDayOfMonth(12), "Electricity",      admin),
            rec(new BigDecimal("200.00"),  RecordType.EXPENSE, "Transport",   LocalDate.now().minusMonths(1).withDayOfMonth(5),  "Fuel",             admin),
            rec(new BigDecimal("150.00"),  RecordType.EXPENSE, "Entertainment",LocalDate.now().minusWeeks(2),                   "Dining",           admin)
        );

        recordRepository.saveAll(records);
        log.info("Seeded {} financial records.", records.size());
        log.info("Ready! H2 console: http://localhost:8080/h2-console");
    }

    private FinancialRecord rec(BigDecimal amount, RecordType type, String category,
                                 LocalDate date, String notes, User createdBy) {
        return FinancialRecord.builder()
                .amount(amount).type(type).category(category)
                .date(date).notes(notes).createdBy(createdBy).build();
    }
}
