package com.xxxx.server.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class TestController {
    @GetMapping("test")
    public String test(){
        return "hello";
    }

    @GetMapping("/employee/basic/test1")
    public String hello2(){
        return "/emp/basic/test";
    }
    @GetMapping("/employee/advanced/test2")
    public String hello3(){
        return "/emp/adv/test";
    }

}
