package com.reggie.common;

//基於ThreadLocal封裝工具類 用戶保存和獲取當前用戶登陸ID

public class BaseContext {
    private  static ThreadLocal<Long> threadLocal = new ThreadLocal<>();


    //設置值
    public static void setCurrentId(Long id){
        threadLocal.set(id);
    }

    //獲取值
    public static Long getCurrentId(){
        return threadLocal.get();
    }

}
