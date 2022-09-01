package com.zjazn.utils;

import com.zjazn.pojo.Up;
import com.zjazn.service.UpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


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
}
