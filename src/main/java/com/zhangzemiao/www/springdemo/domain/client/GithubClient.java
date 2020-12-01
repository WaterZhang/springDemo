package com.zhangzemiao.www.springdemo.domain.client;

import com.zhangzemiao.www.springdemo.domain.feign.HystrixFeignBuilderProvider;
import com.zhangzemiao.www.springdemo.domain.feign.client.NoSSLVerifierClient;
import com.zhangzemiao.www.springdemo.domain.valueobject.Contributor;
import feign.Request;
import feign.gson.GsonDecoder;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GithubClient {
    private HystrixFeignBuilderProvider hystrixFeignBuilderProvider = new HystrixFeignBuilderProvider();
    private final long connectTimeoutMillis;
    private final long readTimeoutMillis;
    private final String url;

    public GithubClient(final @Value("${feign.github.connectTimeoutMillis}") long connectTimeoutMillis,
                        final @Value("${feign.github.readTimeoutMillis}") long readTimeoutMillis,
                        final @Value("${feign.github.url}") String url){
        this.connectTimeoutMillis = connectTimeoutMillis;
        this.readTimeoutMillis = readTimeoutMillis;
        this.url = url;
    }

    public List<Contributor> getContributors(String owner,
                                             String repo){
        final GithubConnector connector = hystrixFeignBuilderProvider
                                              .getCommonBuilder()
                                              .client(new NoSSLVerifierClient())
                                              .decoder(new GsonDecoder())
                                              .options(new Request.Options(connectTimeoutMillis,
                                                                           TimeUnit.MILLISECONDS,
                                                                           readTimeoutMillis,
                                                                           TimeUnit.MILLISECONDS,
                                                                           true))
                                              .traceEnabled(true, Collections.EMPTY_MAP)
                                              .build(GithubConnector.class, url, null);
        return connector.getGithubContributors(owner, repo);
    }



}
