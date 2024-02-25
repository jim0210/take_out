package com.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.reggie.common.CustomException;
import com.reggie.entity.Category;
import com.reggie.entity.Dish;
import com.reggie.entity.Setmeal;
import com.reggie.mapper.CategoryMapper;
import com.reggie.service.CategoryService;
import com.reggie.service.DishService;
import com.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService
{

    @Autowired
    private DishService dishService;
    @Autowired
    private SetmealService setmealService;


    //根據ID刪除分類 刪除之前須進行判斷
    @Override
    public void remove(Long id){
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //添加查詢條件
        dishLambdaQueryWrapper.eq(Dish::getCategoryId,id);
        int count1 = dishService.count(dishLambdaQueryWrapper);
        //查詢當前分類是否關聯菜品 如以關聯 拋出異常
        if(count1>0){
            //已經關聯菜品 拋出業務異常
            throw new CustomException("當前分類下關聯了菜品 不能刪除");
        }

        //查詢當前分類是否關聯套餐 如以關聯 拋出異常
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //添加查詢條件 根據ID進行查詢
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId,id);
        int count2 = setmealService.count(setmealLambdaQueryWrapper);
        if(count2>0){
            //已經關聯套餐 拋出業務異常
            throw new CustomException("當前分類下關聯了套餐 不能刪除");
        }


        //正常刪除
        super.removeById(id);

    }

}
