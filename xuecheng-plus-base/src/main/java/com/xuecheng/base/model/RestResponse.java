package com.xuecheng.base.model;

import lombok.Data;
import lombok.ToString;

/**
 * 通用结果类型
 */
@Data
@ToString
public class RestResponse<T> {

    private RestResponse<T> response;
    /**
     * 响应编码；0 为正常；-1 错误
     */
    private int code;

    /**
     * 响应提示信息
     */
    private String msg;

    /**
     * 响应内容
     */
    private T result;


    public RestResponse() {
        this(0, "success");
    }

    public RestResponse(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * 错误
     */
    public static <T> RestResponse<T> validFail() {
        RestResponse<T> response = new RestResponse<T>();
        response.setCode(-1);
        return response;
    }

    /**
     * 错误信息的封装
     */
    public static <T> RestResponse<T> validFail(String msg) {
        RestResponse<T> response = new RestResponse<T>();
        response.setCode(-1);
        response.setMsg(msg);
        return response;
    }

    /**
     * 错误信息的封装（带包含响应内容）
     */
    public static <T> RestResponse<T> validFail(T result, String msg) {
        RestResponse<T> response = new RestResponse<T>();
        response.setCode(-1);
        response.setResult(result);
        response.setMsg(msg);
        return response;
    }


    /**
     * 正常响应
     */
    public static <T> RestResponse<T> success() {
        return new RestResponse<T>();
    }

    /**
     * 正常响应数据
     */
    public static <T> RestResponse<T> success(T result) {
        RestResponse<T> response = new RestResponse<T>();
        response.setResult(result);
        return response;
    }

    /**
     * 正常响应数据（包含响应信息）
     */
    public static <T> RestResponse<T> success(T result, String msg) {
        RestResponse<T> response = new RestResponse<T>();
        response.setResult(result);
        response.setMsg(msg);
        return response;
    }


    public Boolean isSuccessful() {
        return this.code == 0;
    }

}