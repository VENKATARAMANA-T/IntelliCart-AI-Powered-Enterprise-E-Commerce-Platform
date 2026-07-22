package com.example.Order_Service.dto.response;
import java.time.LocalDateTime;
public class ErrorResponse {
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }
    public String getError() { return error; }
    public void setError(String error) { this.error = error; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getPath() { return path; }
    public void setPath(String path) { this.path = path; }
    public static ErrorResponseBuilder builder() { return new ErrorResponseBuilder(); }
    public static class ErrorResponseBuilder {
        private LocalDateTime timestamp; private int status; private String error; private String message; private String path;
        public ErrorResponseBuilder timestamp(LocalDateTime t) { this.timestamp = t; return this; }
        public ErrorResponseBuilder status(int s) { this.status = s; return this; }
        public ErrorResponseBuilder error(String e) { this.error = e; return this; }
        public ErrorResponseBuilder message(String m) { this.message = m; return this; }
        public ErrorResponseBuilder path(String p) { this.path = p; return this; }
        public ErrorResponse build() {
            ErrorResponse r = new ErrorResponse(); r.setTimestamp(timestamp); r.setStatus(status); r.setError(error); r.setMessage(message); r.setPath(path); return r;
        }
    }
}
