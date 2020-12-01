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

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();
    private Logger logger = LoggerFactory.getLogger(GreetingController.class);

    @GetMapping("/api/greeting.json")
    @ApiOperation(value = "greeting")
    public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name){
        logger.error("test");
        return new Greeting(counter.incrementAndGet(), String.format(template, name));
    }

}
