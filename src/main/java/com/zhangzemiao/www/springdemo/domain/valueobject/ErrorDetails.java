package com.zhangzemiao.www.springdemo.domain.valueobject;

public class ErrorDetails {
    private int errorCode;
    private String errorMessage;

    public ErrorDetails() {
    }

    public ErrorDetails(int errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

}
