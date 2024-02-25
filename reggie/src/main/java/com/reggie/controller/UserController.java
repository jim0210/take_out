package com.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.reggie.common.R;
import com.reggie.entity.User;
import com.reggie.service.UserService;
import com.reggie.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.ognl.EnumerationIterator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;

    //發送手機驗證碼

    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session) {

        //獲取手機號
        String phone = user.getPhone();
        if (StringUtils.isNotEmpty(phone)) {
            //生成隨機四位驗證碼

            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info("code:{}", code);
            //調用短訊服務API完成發送短訊

            //需要將生成的驗證碼保存到session
            session.setAttribute(phone, code);
            return R.success("手機驗證碼短訊發送成功");
        }

        return R.error("短訊發送失敗");
    }

    //移動端登入
    @PostMapping("/login")
    public R<User> login(@RequestBody Map map, HttpSession session) {

        log.info(map.toString());

        //獲取手機號
        String phone = map.get("phone").toString();

        //獲取驗證碼
        String code = map.get("code").toString();

        //從session中獲取保存驗證碼
        Object codeInSession = session.getAttribute(phone);


        //進行驗證碼比對(頁面提交與session中保存驗證碼比對)
        if(codeInSession != null && codeInSession.equals(code)){
            //如果比對成功 說明登陸成功


            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone,phone);

            User user = userService.getOne(queryWrapper);
            if(user == null){
                //判斷當前手機號對應是否為新用戶 如是自動完成註冊
                user = new User();
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);
            }
            session.setAttribute("user",user.getId());
            return R.success(user);
        }



        return R.error("登入失敗");
    }


}
