package me.dworak.rekrutacja.web;

public class ErrorDto {

    private final String message;
    private final String status;

    private ErrorDto(String message, String status) {
        this.message = message;
        this.status = status;
    }

    public static ErrorDto errorDto(String message, String status) {
        return new ErrorDto(message, status);
    }

    public String getMessage() {
        return message;
    }

    public String getStatus() {
        return status;
    }
}
