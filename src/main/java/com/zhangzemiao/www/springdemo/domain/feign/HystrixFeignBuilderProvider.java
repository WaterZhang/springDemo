package com.zhangzemiao.www.springdemo.domain.feign;

public class HystrixFeignBuilderProvider {

    public HystrixFeignBuilder getCommonBuilder() {
        return new HystrixFeignBuilder();
    }

}
