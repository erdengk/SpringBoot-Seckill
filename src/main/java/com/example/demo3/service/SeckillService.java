package com.example.demo3.service;

import com.example.demo3.bean.Seckill;
import com.example.demo3.dto.Exposer;
import com.example.demo3.dto.SeckillExecution;
import com.example.demo3.exception.RepeatKillException;
import com.example.demo3.exception.SeckillCloseException;
import com.example.demo3.exception.SeckillException;

import java.util.List;

/**
 * @author : dk
 * @date : 2019/8/8 16:38
 * @description :
 */


public interface SeckillService {

    /**
     * 获取所有的秒杀商品列表
     *
     * @return
     */
    List<Seckill> findAll();

    /**
     * 获取某一条商品秒杀信息
     *
     * @param seckillId
     * @return
     */
    Seckill getById(Integer seckillId);

    /**
     * 秒杀开始时输出暴露秒杀的地址
     * 否则输出系统时间和秒杀时间
     *
     * @param seckillId
     */
    Exposer exportSeckillUrl(Integer seckillId);

    /**
     * 执行秒杀的操作
     *  @param seckillId
     *
     * @param userPhone
     * @param md5
     * @return
     */
    SeckillExecution executeSeckill(Integer seckillId, String userPhone, String md5)
     throws SeckillException, RepeatKillException, SeckillCloseException;


    /**
     * 执行秒杀的操作  by   存储过程
     *  @param seckillId
     *
     * @param userPhone
     * @param md5
     * @return
     */
    SeckillExecution executeSeckillProducedure(Integer seckillId, String userPhone, String md5)
            throws SeckillException, RepeatKillException, SeckillCloseException;

}
