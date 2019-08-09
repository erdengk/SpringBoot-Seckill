package com.example.demo3.bean;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class Seckill {

  private Integer seckillId;
  private String name;
  private Integer number;
  @DateTimeFormat(pattern ="yyyy-MM-dd HH:mm:ss")
  private java.util.Date startTime;

  @DateTimeFormat(pattern ="yyyy-MM-dd HH:mm:ss")
  private java.util.Date endTime;

  @DateTimeFormat(pattern ="yyyy-MM-dd HH:mm:ss")
  private java.util.Date createTime;


  public Integer getSeckillId() {
    return seckillId;
  }

  public void setSeckillId(Integer seckillId) {
    this.seckillId = seckillId;
  }


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }


  public Integer getNumber() {
    return number;
  }

  public void setNumber(Integer number) {
    this.number = number;
  }


  public java.util.Date getStartTime() {
    return startTime;
  }

  public void setStartTime(java.util.Date startTime) {
    this.startTime = startTime;
  }


  public java.util.Date getEndTime() {
    return endTime;
  }

  public void setEndTime(java.util.Date endTime) {
    this.endTime = endTime;
  }


  public java.util.Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(java.util.Date createTime) {
    this.createTime = createTime;
  }

}
