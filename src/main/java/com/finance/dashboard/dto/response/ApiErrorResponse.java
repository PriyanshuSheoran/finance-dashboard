package com.finance.dashboard.dto.response;

import java.time.LocalDateTime;
import java.util.Map;

public class ApiErrorResponse {

    private int status;
    private String error;
    private String message;
    private LocalDateTime timestamp;
    private Map<String, String> fieldErrors;

    public ApiErrorResponse() {}

    public ApiErrorResponse(int status, String error, String message,
                             LocalDateTime timestamp, Map<String, String> fieldErrors) {
        this.status      = status;
        this.error       = error;
        this.message     = message;
        this.timestamp   = timestamp;
        this.fieldErrors = fieldErrors;
    }

    public int getStatus()                          { return status; }
    public void setStatus(int v)                    { this.status = v; }
    public String getError()                        { return error; }
    public void setError(String v)                  { this.error = v; }
    public String getMessage()                      { return message; }
    public void setMessage(String v)                { this.message = v; }
    public LocalDateTime getTimestamp()             { return timestamp; }
    public void setTimestamp(LocalDateTime v)       { this.timestamp = v; }
    public Map<String, String> getFieldErrors()     { return fieldErrors; }
    public void setFieldErrors(Map<String, String> v) { this.fieldErrors = v; }
}
