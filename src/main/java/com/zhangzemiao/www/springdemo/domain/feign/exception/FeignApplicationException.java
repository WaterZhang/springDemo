package com.zhangzemiao.www.springdemo.domain.feign.exception;

import com.netflix.hystrix.exception.HystrixBadRequestException;

public class FeignApplicationException extends HystrixBadRequestException {
    public FeignApplicationException(String message) {
        super(message);
    }

    public FeignApplicationException(String message, Throwable cause) {
        super(message, cause);
    }
}
