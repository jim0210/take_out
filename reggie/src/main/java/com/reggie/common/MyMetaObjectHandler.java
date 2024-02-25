package com.reggie.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;


//自定義元數據對象處理器

@Component
@Slf4j

public class MyMetaObjectHandler implements MetaObjectHandler {

    //插入操作 自動填充
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("公共自斷自動填充[insert]...");
        log.info(metaObject.toString());
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("createUser", BaseContext.getCurrentId());
        metaObject.setValue("updateUser", BaseContext.getCurrentId());

    }

    //更新操作 自動填充

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("公共自斷自動填充[update]...");
        log.info(metaObject.toString());

        long id = Thread.currentThread().getId();
        log.info("線程ID為: {}", id);


        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("updateUser", BaseContext.getCurrentId());
    }
}
