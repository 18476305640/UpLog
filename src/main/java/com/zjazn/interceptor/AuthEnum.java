package com.zjazn.interceptor;

import lombok.Data;
import lombok.Getter;

@Getter
public enum AuthEnum {
    USER_ID("userId"),
    PASSWORD_MD5("passwordMd5"),
    IS_SYS_USER("isSysUser"),
    UP("up"),
    USER_NAME("userName");

    private String dataName;
    AuthEnum(String dataName) {
        this.dataName = dataName;
    }
}
