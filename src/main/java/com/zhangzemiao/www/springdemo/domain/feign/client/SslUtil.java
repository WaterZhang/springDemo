package com.zhangzemiao.www.springdemo.domain.feign.client;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;

public class SslUtil {

    public static SSLConnectionSocketFactory getSslConnectionSocketFactory() {
        return new SSLConnectionSocketFactory(getSslSocketFactory(), getHostnameVerifier());
    }

    public static SSLSocketFactory getSslSocketFactory() {
        try {
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, getTrustManager(), new SecureRandom());
            return sslContext.getSocketFactory();
        } catch (KeyManagementException | NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

    public static SSLSocketFactory getTlsSocketFactory() {
        try {
            final SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
            sslContext.init(null, null, null);
            return sslContext.getSocketFactory();
        } catch (KeyManagementException | NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

    public static HostnameVerifier getHostnameVerifier() {
        return (s, sslSession) -> true;
    }

    private static TrustManager[] getTrustManager() {
        return new TrustManager[] {
            new X509TrustManager() {
                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }

                @Override
                public void checkClientTrusted(final X509Certificate[] certs, final String authType) {
                    // Nothing to do
                }

                @Override
                public void checkServerTrusted(final X509Certificate[] certs, final String authType) {
                    // Nothing to do
                }
            }
        };
    }

}
