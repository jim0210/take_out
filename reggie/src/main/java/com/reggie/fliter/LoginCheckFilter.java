package com.reggie.fliter;

import com.alibaba.fastjson.JSON;
import com.reggie.common.BaseContext;
import com.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//檢查用戶是否已經登陸
@WebFilter(filterName = "loginCheckFilter",urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {
    //路徑匹配器,支持通佩符
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        //1.獲取本次請求URI
        String requestURI = request.getRequestURI();//backend/index.html

        log.info("攔截到請求: {}",requestURI);

        //定義不需要處理的路徑
        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/common/**",
                "/user/sendMsg",
                "/user/login"
        };


        //2.判斷本次請求是否需要處理
        boolean check = check(urls, requestURI);


        //3.如不需處理 則放行
        if(check){
            log.info("本次請求{}不需要處理",requestURI);
            filterChain.doFilter(request, response);
            return;
        }
        //4-1.判斷登陸狀態 如登陸 則放行
        if(request.getSession().getAttribute("employee") != null){
            log.info("用戶已登錄, 用戶ID為: {}", request.getSession().getAttribute("employee"));

            Long empId = (Long)request.getSession().getAttribute("employee");
            BaseContext.setCurrentId(empId);

            filterChain.doFilter(request, response);
            return;
        }
        //4-2.判斷登陸狀態 如登陸 則放行 行動裝置
        if(request.getSession().getAttribute("user") != null){
            log.info("用戶已登錄, 用戶ID為: {}", request.getSession().getAttribute("user"));

            Long empId = (Long)request.getSession().getAttribute("user");
            BaseContext.setCurrentId(empId);

            filterChain.doFilter(request, response);
            return;
        }

        log.info("用戶未登錄");
        //5.如未登錄則返回登陸結果 通過輸出流方式向客戶端頁面響應數據
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;



    }
    //路徑匹配 檢查本次請求是否需要放行
    public boolean check(String[] urls, String requestURI){
        for (String url : urls){
            boolean match = PATH_MATCHER.match(url, requestURI);
            if(match){
                return true;

            }
        }
        return false;

    }
}
