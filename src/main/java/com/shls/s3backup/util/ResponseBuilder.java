package com.shls.s3backup.util;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author ：Killer
 * @date ：Created in 20-6-11 下午5:21
 * @description：${description}
 * @modified By：
 * @version: version
 */
public class ResponseBuilder {
    public static int SUCCESS = 200;
    public static int ERROR = 500;
    public static int NOT_FOUND = 404;
    public static int UNAUTHORIZED = 401;
    public static int REDIRECT = 302;
    private Map<String, Object> responseMap = new LinkedHashMap();

    public ResponseBuilder() {
    }

    public ResponseBuilder success() {
        this.code(SUCCESS);
        this.message("success");
        return this;
    }

    public ResponseBuilder error() {
        this.code(ERROR);
        this.message("error");
        return this;
    }

    public ResponseBuilder unauthorized() {
        this.code(UNAUTHORIZED);
        this.message("unauthorized");
        return this;
    }

    public ResponseBuilder code(int code) {
        this.responseMap.put("code", code);
        return this;
    }

    public ResponseBuilder message(String message) {
        this.responseMap.put("message", message);
        return this;
    }

    public ResponseBuilder data(Object data) {
        this.responseMap.put("data", data);
        return this;
    }

    public ResponseBuilder add(String name, Object value) {
        this.responseMap.put(name, value);
        return this;
    }

    public Map build() {
        return this.responseMap;
    }
}