package com.ruanchuang.handler;

import java.util.HashMap;
import java.util.Map;

/**
 * 请求上下文, 基于ThreadLocal实现, 解决非Web上下文无法获取用户token问题
 * @Author guopeixiong
 * @Date 2023/8/3
 * @Email peixiongguo@163.com
 */
public class RequestContextHandler {

    public static ThreadLocal<Map<String, Object>> threadLocal = new ThreadLocal<>();

    public static final String USER_TOKEN_KEY = "user:token";

    public static void set(String key, Object value) {
        Map<String, Object> map = threadLocal.get();
        if (map == null) {
            map = new HashMap<>();
            threadLocal.set(map);
        }
        map.put(key, value);
    }

    public static Object get(String key) {
        Map<String, Object> map = threadLocal.get();
        if (map == null) {
            map = new HashMap<>();
            threadLocal.set(map);
        }
        return map.get(key);
    }

    public static void remove() {
        threadLocal.remove();
    }

    public static void setUserToken(String token) {
        set(USER_TOKEN_KEY, token);
    }

    public static String getUserToken() {
        Object token = get(USER_TOKEN_KEY);
        if (token == null) {
            return null;
        }
        return (String) token;
    }

}
