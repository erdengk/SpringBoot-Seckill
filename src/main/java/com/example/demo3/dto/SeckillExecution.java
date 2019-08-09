package com.example.demo3.dto;

import com.example.demo3.bean.SuccessKilled;
import com.example.demo3.enums.SeckillStatEnum;
import lombok.Data;

/**
 * 封装执行秒杀后的结果
 *
 */

@Data
public class SeckillExecution {

    private Integer seckillId;

    //秒杀执行结果状态
    private int state;

    //状态表示
    private String stateInfo;

    //秒杀成功的订单对象
    private SuccessKilled successKilled;

    public SeckillExecution(Integer seckillId, SeckillStatEnum seckillStatEnum, String stateInfo, SuccessKilled successKilled) {
        this.seckillId = seckillId;
        this.state = seckillStatEnum.getState();
        this.stateInfo = stateInfo;
        this.successKilled = successKilled;
    }

    public SeckillExecution(Integer seckillId, SeckillStatEnum seckillStatEnum, String stateInfo) {
        this.seckillId = seckillId;
        this.state = seckillStatEnum.getState();
        this.stateInfo = stateInfo;
    }

    public SeckillExecution(Integer seckillId, SeckillStatEnum seckillStatEnum, SuccessKilled successKilled) {
        this.seckillId = seckillId;
        this.state = seckillStatEnum.getState();
        this.successKilled = successKilled;
    }

    public SeckillExecution(Integer seckillId, SeckillStatEnum seckillStatEnum) {
        this.seckillId = seckillId;
        this.state = seckillStatEnum.getState();
    }

    public Integer getSeckillId() {
        return seckillId;
    }

    public void setSeckillId(Integer seckillId) {
        this.seckillId = seckillId;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public void setStateInfo(String stateInfo) {
        this.stateInfo = stateInfo;
    }



}
