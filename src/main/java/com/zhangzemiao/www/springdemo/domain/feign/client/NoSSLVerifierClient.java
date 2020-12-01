package com.zhangzemiao.www.springdemo.domain.feign.client;

import com.zhangzemiao.www.springdemo.domain.feign.client.SslUtil;
import feign.Client;

public class NoSSLVerifierClient extends Client.Default {

    public NoSSLVerifierClient() {
        super(SslUtil.getSslSocketFactory(), SslUtil.getHostnameVerifier());
    }
}
