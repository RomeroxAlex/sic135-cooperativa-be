package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

public class ErrorResponseDto implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private String code;
    private String message;
    private String details;
    private LocalDateTime timestamp;

    public ErrorResponseDto() {
        this.timestamp = LocalDateTime.now();
    }

    public ErrorResponseDto(String code, String message, String details) {
        this();
        this.code = code;
        this.message = message;
        this.details = details;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
