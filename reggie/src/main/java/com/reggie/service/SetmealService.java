package com.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.reggie.dto.SetmealDto;
import com.reggie.entity.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal>{
    //新增套餐 同時需要保存套餐和菜品的關聯關係
    public void saveWithDish(SetmealDto setmealDto);

    //刪除套餐 同時需要刪除套餐和菜品的關聯數據
    public void removeWithDish(List<Long> ids);
}
