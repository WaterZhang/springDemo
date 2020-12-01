package com.zhangzemiao.www.springdemo.domain.valueobject;

public interface IResponseMessage<T> {

    T getResponse();
    ErrorDetails getErrorDetails();
    boolean isSuccessful();

}
