package com.zhangzemiao.www.springdemo.domain.valueobject;

public class ErrorDetails {
    private int errorCode;
    private String errorMessage;

    public ErrorDetails() {
        //NO PMD
    }

    public ErrorDetails(final int errorCode, final String errorMessage) {
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
