package com.zjazn.interceptor;

import com.zjazn.pojo.Up;
import com.zjazn.service.UpService;
import com.zjazn.utils.CookieUtils;
import com.zjazn.utils.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class AuthIntercepter implements HandlerInterceptor {
    /**
     * 会在handler方法业务逻辑执行之前执行
     * 往往在这里完成权限校验工作
     * @param request
     * @param response
     * @param handler
     * @return  返回值boolean代表是否放行，true代表放行，false代表中止
     * @throws Exception
     */
    @Autowired
    private UpService upService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String userName = CookieUtils.getCookie(request, "user");
        String passwordMd5 = CookieUtils.getCookie(request, "scode");


        if (userName != null && passwordMd5 != null) {
            Boolean isSysUser = Root.isSysUser(upService,userName, passwordMd5);
            if (! isSysUser) return true;
            Up up = upService.queryByUserName(userName);
            request.setAttribute("userName",userName);
            request.setAttribute("userId",up.getUp_id());
            request.setAttribute("passwordMd5",passwordMd5);
            request.setAttribute("isSysUser",isSysUser);
            request.setAttribute("up",up);
        }

//        System.out.println("MyIntercepter01 preHandle......");
        return true;
    }


    /**
     * 会在handler方法业务逻辑执行之后尚未跳转页面时执行
     * @param request
     * @param response
     * @param handler
     * @param modelAndView  封装了视图和数据，此时尚未跳转页面呢，你可以在这里针对返回的数据和视图信息进行修改
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
//        System.out.println("MyIntercepter01 postHandle......");
    }

    /**
     * 页面已经跳转渲染完毕之后执行
     * @param request
     * @param response
     * @param handler
     * @param ex  可以在这里捕获异常
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
//        System.out.println("MyIntercepter01 afterCompletion......");
    }
}
