package com.zjazn.interceptor.auth;

import javax.servlet.http.HttpServletRequest;

public class AuthUtils {
    /*获取放在request上的用户信息
    * 服务于Root.java
    * */
    public static <T> T getDataByHttpRequest(HttpServletRequest request, String dataName, Class<T> t) {
        Object attribute = request.getAttribute(dataName);
        if (attribute == null) return null;
        try {
            T t_obj = (T)attribute;
            return t_obj;
        }catch (Exception e) {
            return null;
        }
    }

}
