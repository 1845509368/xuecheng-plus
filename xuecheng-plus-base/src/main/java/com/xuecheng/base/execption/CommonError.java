package com.xuecheng.base.execption;


import lombok.Getter;

/**
 * 通用错误信息（枚举类）
 */
@Getter
public enum CommonError {

    // 枚举项
    UNKNOWN_ERROR("执行过程异常，请重试。"),
    PARAMS_ERROR("非法参数"),
    OBJECT_NULL("对象为空"),
    QUERY_NULL("查询结果为空"),
    REQUEST_NULL("请求参数为空");

    /**
     * 获取错误信息
     */
    private final String errMessage;

    /**
     * 构造方法
     * 传一个枚举，返回一个信息
     */
    CommonError(String errMessage) {
        this.errMessage = errMessage;
    }

}
