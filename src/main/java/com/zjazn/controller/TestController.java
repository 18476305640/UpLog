package com.zjazn.controller;

import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/test")
public class TestController {
    @RequestMapping("/sendCommand")
    public String toAddReport(String command){
        System.out.println(command);
        if (command.equals("wait")) {
            System.out.println("长等待~");
            try {
                Thread.sleep(2000000000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }else {
            System.out.println("立刻返回结束~");
        }

        return "{}";
    }


}
