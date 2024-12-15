package com.xuecheng.base.execption;


import lombok.Getter;

/**
 * 学成在线项目异常类
 */
@Getter
public class XueChengPlusException extends RuntimeException {

   private final String errMessage;

   public XueChengPlusException(String errMessage) {
      super(errMessage);
      this.errMessage = errMessage;
   }

   /**
    * 抛出异常
    * @param commonError 通用错误枚举类型
    */
   public static void cast(CommonError commonError){
       throw new XueChengPlusException(commonError.getErrMessage());
   }

   /**
    * 抛出异常
    * @param errMessage 异常信息
    */
   public static void cast(String errMessage){
       throw new XueChengPlusException(errMessage);
   }

}