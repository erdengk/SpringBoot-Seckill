package com.example.demo3.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class SuccessKilled {

  private Integer seckillId;
  private String userPhone;
  private Integer state;
  @DateTimeFormat(pattern ="yyyy-MM-dd HH:mm:ss")

  @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
  private java.util.Date createTime;

  private Seckill seckill;

  public Integer getSeckillId() {
    return seckillId;
  }

  public void setSeckillId(Integer seckillId) {
    this.seckillId = seckillId;
  }


  public String getUserPhone() {
    return userPhone;
  }

  public void setUserPhone(String userPhone) {
    this.userPhone = userPhone;
  }


  public Integer getState() {
    return state;
  }

  public void setState(Integer state) {
    this.state = state;
  }


  public java.util.Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(java.util.Date createTime) {
    this.createTime = createTime;
  }

}
