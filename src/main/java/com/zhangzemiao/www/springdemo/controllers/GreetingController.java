package com.zhangzemiao.www.springdemo.controllers;

import com.zhangzemiao.www.springdemo.domain.valueobject.Greeting;
import io.swagger.annotations.ApiOperation;
import java.util.concurrent.atomic.AtomicLong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {

    private static final String TEMPLATE = "Hello, %s!";
    private static final Logger LOGGER = LoggerFactory.getLogger(GreetingController.class);
    private final AtomicLong counter = new AtomicLong();

    @GetMapping("/api/greeting.json")
    @ApiOperation(value = "greeting")
    public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") final String name){
        LOGGER.error("test");
        return new Greeting(counter.incrementAndGet(), String.format(TEMPLATE, name));
    }

}
