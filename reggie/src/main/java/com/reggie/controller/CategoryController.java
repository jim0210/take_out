package com.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.reggie.common.R;
import com.reggie.entity.Category;
import com.reggie.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;


//分類管理
@RestController
@RequestMapping("/category")
@Slf4j
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    //新增分類
    @PostMapping
    public R<String> save(@RequestBody Category category){
        log.info("category: {}", category);
        categoryService.save(category);
        return R.success("新增分類成功");
    }

    //分頁查詢
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize){
        //分頁構造器
        Page<Category> pageInfo = new Page<>(page, pageSize);
        //條件構造器
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        //添加排序條件
        queryWrapper.orderByAsc(Category::getSort);
        //進行分頁查詢
        categoryService.page(pageInfo, queryWrapper);

        return R.success(pageInfo);

    }

    //根據ID刪除分類
    @DeleteMapping
    public R<String> delete(Long ids){
        log.info("刪除分類, id為: {}", ids);

        //categoryService.removeById(ids);
        categoryService.remove(ids);

        return R.success("分類訊息刪除成功");
    }


    //根據ID修改訊息
    @PutMapping
    public R<String> update(@RequestBody Category category){
        log.info("修改分類訊息: {}", category);
        categoryService.updateById(category);

        return R.success("修改分類訊息成功");

    }


    //根據條件查詢分類數據
    @GetMapping("/list")
    public R<List<Category>> list(Category category){
        //條件構造器
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        //添加條件
        queryWrapper.eq(category.getType() !=null, Category::getType, category.getType());
        //添加排序條件
        queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);

        List<Category> list = categoryService.list(queryWrapper);


        return R.success(list);

    }
}
