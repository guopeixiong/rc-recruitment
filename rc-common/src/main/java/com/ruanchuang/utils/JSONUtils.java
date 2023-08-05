package com.ruanchuang.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

/**
 * @Author guopeixiong
 * @Date 2023/8/5
 * @Email peixiongguo@163.com
 */
@Slf4j
public class JSONUtils {

    private static ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    public static String toJsonString(Object o) {
        if (o == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
           log.warn("json序列化异常, 异常信息: '{}'", e.getMessage());
        }
        return null;
    }

    public static <T> T jsonToObject(String json, Class<T> t) {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        try {
            return objectMapper.readValue(json.getBytes(), t);
        } catch (IOException e) {
            log.warn("json反序列化异常, 异常信息: '{}'", e.getMessage());
        }
        return null;
    }

}
