package com.cards.cards.dtos;

import java.time.LocalDateTime;

public class ExceptionObj {
    private LocalDateTime timestamp;
    private String errorMessage;
    private String errorDescription;

    public ExceptionObj(LocalDateTime timestamp, String errorMessage, String errorDescription) {
        this.timestamp = timestamp;
        this.errorMessage = errorMessage;
        this.errorDescription = errorDescription;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }
}
