package com.zjazn.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zjazn.data.CommentAndUp;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class BaiduController {
    @RequestMapping("/baidu_verify_code-RoJ6ikOGnY.html")
    @ResponseBody
    public String queryByLogId(Integer commentLogId) throws JsonProcessingException {
        return "06ff2e11fc3cc1a9be867ab424817002";
    }
}
