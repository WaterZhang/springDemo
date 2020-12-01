package com.zhangzemiao.www.springdemo.domain.feign.log;

import feign.Response;
import feign.RetryableException;
import feign.Util;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

public class Utils {
    public static String asSplunkLogString(Map<String, String> parameters) {
        StringBuilder out = new StringBuilder();
        for (String name:  parameters.keySet()) {
            String value = Objects.toString(parameters.get(name));
            out.append(String.format("%s=%s, ", Objects.toString(name), value));
        }
        return out.toString();
    }


    public static String asSplunkMessage(String message) {
        return String.format("[%s]", Objects.toString(message));
    }

    public static String decodeOrDefault(byte[] data, Charset charset, String defaultValue) {
        if (data == null) {
            return defaultValue;
        }
        Util.checkNotNull(charset, "charset");
        try {
            return charset.newDecoder().decode(ByteBuffer.wrap(data)).toString();
        } catch (CharacterCodingException ex) {
            return defaultValue;
        }
    }

    public static <T> T defaultIfNull(T object, T defaultObject) {
        return object == null ? defaultObject : object;
    }

    public static RetryableException convertToRetryableException(IOException e, Response response) {
        return new RetryableException(
            response.status(),
            String.format("IO exception while reading response: %s, url: %s" ,
                          e.getMessage(), response.request().url()),
            response.request().httpMethod(), new Date(), null);
    }
}
