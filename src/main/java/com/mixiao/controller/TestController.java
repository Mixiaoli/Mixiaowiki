package com.mixiao.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController //返回字符串 或JSON对象(更多是返回JSON对象)
@Controller //返回一个页面
public class TestController {

    @RequestMapping("/hello") //定义URL路径 表示这个接口支持所以请求方式 POST GET...
    public String hello(){
        return "hello world";
    }
}
