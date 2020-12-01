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
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, getTrustManager(), new SecureRandom());
            return sslContext.getSocketFactory();
        } catch (KeyManagementException | NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

    public static SSLSocketFactory getTlsSocketFactory() {
        try {
            SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
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
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }

                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                    // Nothing to do
                }

                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                    // Nothing to do
                }
            }
        };
    }

}
