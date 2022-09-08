package com.zjazn.interceptor.auth;

import lombok.Getter;

@Getter
public enum  RootEnum {
    SYS_USER(0,"普通用户"),
    BASE_ROOT_USER(100,"管理员"),
    S_ROOT_USER(1000,"超级管理员");

    private Integer rootValue;
    private String rootName;
    RootEnum(Integer rootValue,String rootName) {
        this.rootValue = rootValue;
        this.rootName = rootName;
    }

}
