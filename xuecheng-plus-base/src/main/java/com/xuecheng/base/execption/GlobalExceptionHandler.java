package com.xuecheng.base.execption;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

/**
 * 全局异常处理器
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

   /**
    * 处理自定义异常
    */
   @ExceptionHandler(XueChengPlusException.class)
   @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) //状态码
   public RestErrorResponse customException(XueChengPlusException e) {
      log.error("【系统异常】{}",e.getErrMessage(),e);
      return new RestErrorResponse(e.getErrMessage());
   }

   /**
    * 处理未知异常
    */
   @ExceptionHandler(Exception.class)
   @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
   public RestErrorResponse exception(Exception e) {
      if(e.getMessage().equals("不允许访问")){
         return new RestErrorResponse("没有操作此功能的权限");
      }
      log.error("【系统异常】{}",e.getMessage(),e);
      return new RestErrorResponse(CommonError.UNKNOWN_ERROR.getErrMessage());
   }

   /**
    * 处理JSR校验异常
    */
   @ExceptionHandler(MethodArgumentNotValidException.class)
   @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
   public RestErrorResponse methodArgumentNotValidException(MethodArgumentNotValidException e) {
      BindingResult bindingResult = e.getBindingResult();
      List<String> msgList = new ArrayList<>();
      //将错误信息放在msgList
      bindingResult.getFieldErrors().forEach(item->msgList.add(item.getDefaultMessage()));
      //拼接错误信息
      String msg = StringUtils.join(msgList, ",");
      log.error("【系统异常】{}",msg);
      return new RestErrorResponse(msg);
   }
}