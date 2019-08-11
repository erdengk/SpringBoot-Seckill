package com.example.demo3.controller;

import com.example.demo3.bean.Seckill;
import com.example.demo3.dto.Exposer;
import com.example.demo3.dto.SeckillExecution;
import com.example.demo3.dto.SeckillResult;
import com.example.demo3.enums.SeckillStatEnum;
import com.example.demo3.exception.RepeatKillException;
import com.example.demo3.exception.SeckillCloseException;
import com.example.demo3.exception.SeckillException;
import com.example.demo3.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @author : dk
 * @date : 2019/8/9 10:55
 * @description : 秒杀的相关控制器
 */

@Controller
@RequestMapping("/seckill")
public class SeckillController {

    @Autowired
    private SeckillService seckillService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    @RequestMapping(value = "/list" ,method = RequestMethod.GET)
    public String list(Model model)
    {
        List<Seckill> list = seckillService.findAll();
        model.addAttribute("list", list);
        return "seckill";
    }

    @RequestMapping(value = "/{seckillId}/detail",method = RequestMethod.GET)
    public String detail(@PathVariable("seckillId") Integer seckillId, Model model) {
        if (seckillId == null) {
            return "seckill";
        }
        Seckill seckill = seckillService.getById(seckillId);
        if (seckill == null) {
            return "seckill";
        }
        model.addAttribute("seckill", seckill);
        return "seckill_detail";
    }

    @ResponseBody
    @RequestMapping(value = "/{seckillId}/exposer",
            method = RequestMethod.POST,
            produces = {"application/json;charset=UTF-8"}
            )
    public SeckillResult<Exposer> exposer(@PathVariable("seckillId") Integer seckillId) {
        SeckillResult<Exposer> result;
        try {
            Exposer exposer = seckillService.exportSeckillUrl(seckillId);
            result = new SeckillResult<Exposer>(true, exposer);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            logger.error(e.getMessage(), e);
            result = new SeckillResult<Exposer>(false, e.getMessage());
        }
        return result;
    }

    @RequestMapping(value = "/{seckillId}/{md5}/execution",
            method = RequestMethod.POST,
            produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public SeckillResult <SeckillExecution> execute(@PathVariable("seckillId") Integer seckillId,
                                                   @PathVariable("md5") String md5,
                                                   @CookieValue(value = "killPhone", required = false) String userPhone) {
        //System.out.println("-------------"+userPhone);
        if (userPhone == null) {
            return new SeckillResult<SeckillExecution>(false, "未注册");
        }
        try {
            SeckillExecution seckillExecution = seckillService.executeSeckillProducedure(seckillId,userPhone, md5);
            System.out.println("seckillExecution"+seckillExecution);
            return new SeckillResult<SeckillExecution>(true, seckillExecution);
        } catch (RepeatKillException e) {
            SeckillExecution seckillExecution = new SeckillExecution(seckillId, SeckillStatEnum.REPEAT_KILL);
            System.out.println("seckillExecution"+seckillExecution);
            return new SeckillResult<SeckillExecution>(true, seckillExecution);
        } catch (SeckillCloseException e) {
            SeckillExecution seckillExecution = new SeckillExecution(seckillId, SeckillStatEnum.END);
            System.out.println("seckillExecution"+seckillExecution);
            return new SeckillResult<SeckillExecution>(true, seckillExecution);
        } catch (SeckillException e) {
            SeckillExecution seckillExecution = new SeckillExecution(seckillId, SeckillStatEnum.INNER_ERROR);
            System.out.println("seckillExecution"+seckillExecution);
            return new SeckillResult<SeckillExecution>(true, seckillExecution);
        }
    }

    /**
     * 获取系统时间
     * @return
     */
    @ResponseBody
    @GetMapping(value = "/time/now")
    public SeckillResult<Long> time() {
        Date now = new Date();
        return new SeckillResult(true, now.getTime());
    }


}
