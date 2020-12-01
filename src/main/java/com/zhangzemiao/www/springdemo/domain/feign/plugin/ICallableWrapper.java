package com.zhangzemiao.www.springdemo.domain.feign.plugin;

import java.util.concurrent.Callable;

public interface ICallableWrapper {

    <T> Callable<T> wrapCallable(Callable<T> callable);
}
