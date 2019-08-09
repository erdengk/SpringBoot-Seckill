package com.example.demo3.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author : dk
 * @date : 2019/8/9 10:30
 * @description :
 */
@Controller
public class TestController {
    @RequestMapping("/")
    public String s()
    {
        System.out.println(1);
        return "1";
    }
}
