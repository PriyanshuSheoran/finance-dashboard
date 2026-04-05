package com.finance.dashboard.entity;

/**
 * Defines the three access levels in the system.
 *
 * VIEWER  → read-only dashboard access
 * ANALYST → read + summary/analytics access
 * ADMIN   → full CRUD on records and users
 */
public enum Role {
    VIEWER,
    ANALYST,
    ADMIN
}
