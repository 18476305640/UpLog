package com.zjazn.interceptor.auth;

import com.zjazn.pojo.Up;
import com.zjazn.service.UpService;
import com.zjazn.utils.MD5;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;


@Component
public class Root {

    @Autowired
    private static UpService upService;

    public static Boolean isSysUser( String username, String scode){
        return isSysUser(upService,username,scode);
    }
    public static Boolean isSysUser( UpService upService, String username, String scode){
        Up up = upService.queryByUserName(username);
        if(scode.equals(MD5.encrypByMd5(up.getUp_password()))){
            return true;
        }
        return false;
    }
    /*
    *
    * request.setAttribute("userName",userName);
            request.setAttribute("userId",up.getUp_id());
            request.setAttribute("passwordMd5",passwordMd5);
            request.setAttribute("isSysUser",isSysUser);
            request.setAttribute("up",up);
    * */
    public static Boolean isSysUser(HttpServletRequest request) {
        Boolean isSysUser = AuthUtils.getDataByHttpRequest(request, AuthEnum.IS_SYS_USER.getDataName(), Boolean.class);
        Up up = AuthUtils.getDataByHttpRequest(request, AuthEnum.UP.getDataName(), Up.class);
        if (isSysUser){
            return true;
        }
        return false;
    }
    public static Boolean isSysUser(HttpServletRequest request, Integer upId) {
        Boolean isSysUser = AuthUtils.getDataByHttpRequest(request, AuthEnum.IS_SYS_USER.getDataName(), Boolean.class);
        Up up = AuthUtils.getDataByHttpRequest(request, AuthEnum.UP.getDataName(), Up.class);
        if (isSysUser && up.getUp_id() == upId){
            return true;
        }
        return false;
    }
}
