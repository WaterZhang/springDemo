package com.zhangzemiao.www.springdemo.domain.feign.exception;

import com.netflix.hystrix.exception.HystrixBadRequestException;

public class FeignApplicationException extends HystrixBadRequestException {
    public FeignApplicationException(final String message) {
        super(message);
    }

    public FeignApplicationException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
