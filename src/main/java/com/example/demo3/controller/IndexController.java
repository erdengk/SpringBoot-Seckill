package com.example.demo3.controller;

import com.example.demo3.bean.Seckill;
import com.example.demo3.service.SeckillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author : dk
 * @date : 2019/8/9 10:30
 * @description :
 */
@Controller
public class IndexController {

    @Autowired
    private SeckillService seckillService;
    @RequestMapping("/")
    public String list(Model model)
    {
        List<Seckill> list = seckillService.findAll();
        model.addAttribute("list", list);
        return "seckill";
    }
}
