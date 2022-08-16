package com.zjazn.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {
    @RequestMapping("/")
    public String toIndex() {
        System.out.println("准备跳转到首页！！");
        return "index_";
    }

}
