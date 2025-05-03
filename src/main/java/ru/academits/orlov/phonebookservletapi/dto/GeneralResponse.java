package ru.academits.orlov.phonebookservletapi.dto;

public class GeneralResponse {
    private boolean success;
    private String message;

    public GeneralResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static GeneralResponse getSuccessResponse() {
        return new GeneralResponse(true, null);
    }

    public static GeneralResponse getErrorResponse(String message) {
        return new GeneralResponse(false, message);
    }
}
