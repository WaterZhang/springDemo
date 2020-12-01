package com.zhangzemiao.www.springdemo.domain.client;

import com.zhangzemiao.www.springdemo.domain.valueobject.Contributor;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import java.util.List;

public interface GithubConnector {

    @Headers("Content-Type: application/json")
    @RequestLine("GET /repos/{owner}/{repo}/contributors")
    List<Contributor> contributors(@Param("owner") String owner,
                                   @Param("repo") String repo);

}
