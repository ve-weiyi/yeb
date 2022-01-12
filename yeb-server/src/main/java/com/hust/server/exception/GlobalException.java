package com.hust.server.exception;

import com.hust.server.pojo.RespBean;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

/**
 * @program: yeb
 * @description: 全局异常处理
 * @author: Honors
 * @create: 2021-07-16 14:56
 */
@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler(SQLException.class)
    public RespBean mySqlException(SQLException e){
        if (e instanceof SQLIntegrityConstraintViolationException){
            return RespBean.error("该数据有关联数据，操作失败！");
        }
        //除了上面捕获的知道sql异常，其他sql异常都报这个错误
        return RespBean.error("数据库异常，操作失败!");
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public RespBean mySqlException(HttpMessageNotReadableException e){
        if(e instanceof HttpMessageNotReadableException){
            return RespBean.error("请求失败，请检查传入参数是否符合JSON格式！");
        }
        //除了上面捕获的知道sql异常，其他sql异常都报这个错误
        return RespBean.error("数据库异常，操作失败!");
    }
}