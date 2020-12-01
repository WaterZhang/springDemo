package com.zhangzemiao.www.springdemo.domain.mapper;

import com.zhangzemiao.www.springdemo.domain.valueobject.IResponseMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class ResponseEntityMapper {

    public <T extends IResponseMessage> ResponseEntity<T> mapWithRequestId(final T message) {
        if (!message.isSuccessful()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
        } else {
            return ResponseEntity.ok(message);
        }
    }
}
