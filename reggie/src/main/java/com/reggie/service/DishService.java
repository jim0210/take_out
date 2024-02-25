package com.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.reggie.dto.DishDto;
import com.reggie.entity.Dish;

public interface DishService extends IService<Dish> {

    //新增菜品 同時插入菜品對應的口味數據 需要操作兩張表 dish dish_flavor
    public void saveWithFlavor(DishDto dishDto);

    //根據ID查詢菜品訊息和對應口味的訊息
    public DishDto getByIdWithFlavor(Long id);

    //更新菜品訊息 同時更新對應口味訊息
    public void updateWithFlavor(DishDto dishDto);
}
