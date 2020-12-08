package com.zhangzemiao.www.springdemo.domain.feign.plugin;

import com.netflix.hystrix.HystrixThreadPoolKey;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategy;
import com.netflix.hystrix.strategy.properties.HystrixProperty;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.springframework.util.Assert;

public class ContextAwareConcurrencyStrategy extends HystrixConcurrencyStrategy {
    private final Collection<ICallableWrapper> wrappers;
    private final boolean allowCoreThreadTimeOut;

    public ContextAwareConcurrencyStrategy(final Collection<ICallableWrapper> wrappers,
                                           final boolean allowCoreThreadTimeOut) {
        super();
        Assert.notNull(wrappers, "Parameter wrapper collection should be initialized");
        this.wrappers = wrappers;
        this.allowCoreThreadTimeOut = allowCoreThreadTimeOut;
    }

    @Override
    public ThreadPoolExecutor getThreadPool(final HystrixThreadPoolKey threadPoolKey,
                                            final HystrixProperty<Integer> corePoolSize,
                                            final HystrixProperty<Integer> maximumPoolSize,
                                            final HystrixProperty<Integer> keepAliveTime,
                                            final TimeUnit unit,
                                            final BlockingQueue<Runnable> workQueue) {

        final ThreadPoolExecutor threadPoolExecutor =
            super.getThreadPool(threadPoolKey, corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);

        threadPoolExecutor.allowCoreThreadTimeOut(this.allowCoreThreadTimeOut);
        return threadPoolExecutor;
    }

    @Override
    public <T> Callable<T> wrapCallable(final Callable<T> callable) {
        return new CallableWrapperChain<>(callable, wrappers.iterator()).wrapCallable();
    }

    private static class CallableWrapperChain<T> {

        private final Callable<T> callable;

        private final Iterator<ICallableWrapper> wrappers;

        public CallableWrapperChain(final Callable<T> callable, final Iterator<ICallableWrapper> wrappers) {
            this.callable = callable;
            this.wrappers = wrappers;
        }

        public Callable<T> wrapCallable() {
            Callable<T> result = callable;
            while (wrappers.hasNext()) {
                result = wrappers.next().wrapCallable(result);
            }
            return result;
        }
    }
}
