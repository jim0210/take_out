package com.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.reggie.common.R;
import com.reggie.entity.Employee;
import com.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.filters.ExpiresFilter;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    //員工登陸
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request,@RequestBody Employee employee){
        //1.將頁面提交的密碼進行MD5加密處理
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        //2.根據頁面提交的用戶名username查詢數據庫
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);

        //3.如果沒有查詢到則返回登陸失敗結果
        if(emp == null){
            return R.error("登陸失敗");
        }
        //4.密碼比對 如果不一樣則返回登陸失敗結果
        if(!emp.getPassword().equals(password)){
            return R.error("登陸失敗");
        }
        //5.查看員工狀態 如果禁用 則返回員工禁用結果
        if(emp.getStatus() == 0){
            return R.error("帳號已禁用");
        }
        //6.登陸成功 將員工id存入session並返回登陸成功結果
        request.getSession().setAttribute("employee",emp.getId());
            return R.success(emp);


    }

    //員工退出
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        //清理session中保存的當前登陸員工ID
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }

    //新增員工
    @PostMapping
    public  R<String> save(HttpServletRequest request, @RequestBody Employee employee){
        log.info("新增員工, 員工訊息: {}",employee. toString());
        //設置初始密碼123456 禁行MD5加密
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));

        //employee.setCreateTime(LocalDateTime.now());
        //employee.setUpdateTime(LocalDateTime.now());

        //獲得當前用戶ID
        //Long empId =(long) request.getSession().getAttribute("employee");
        //employee.setCreateUser(empId);
        //employee.setUpdateUser(empId);

        employeeService.save(employee);

        return R.success("新增員工成功");

    }
    //員工訊息分頁查詢

    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name){
        log.info("page = {}, pageSize = {}, name = {}" ,page,pageSize,name);

        //構造分頁構造器
        Page pageInfo = new Page(page, pageSize);
        //構造條件構造器
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper();
        //添加過濾條件
        queryWrapper.like(StringUtils. isNotEmpty(name),Employee::getName, name);
        //排序條件
        queryWrapper.orderByDesc(Employee::getUpdateTime);
        //執行查詢
        employeeService.page(pageInfo, queryWrapper);
        return R.success(pageInfo);


    }
    //根據ID修改員工訊息
    @PutMapping
    public R<String> update(HttpServletRequest request, @RequestBody Employee employee){

        long id = Thread.currentThread().getId();
        log.info("線程ID為: {}", id);

        //Long empId = (Long) request.getSession().getAttribute("employee");
        //employee.setUpdateTime(LocalDateTime.now());
        //employee.setUpdateUser(empId);
        employeeService.updateById(employee);
        return R.success("員工訊息修改成功");

    }

    //根據ID查詢員工訊息

    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable  Long id){
        log.info("根據ID查詢員工訊息...");
        Employee employee = employeeService.getById(id);
        if(employee != null){
            return R.success(employee);
        }
        return R.error("沒有查詢到對應員工訊息");
    }

}






