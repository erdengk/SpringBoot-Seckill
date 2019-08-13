package com.example.demo3.mapper;

import com.example.demo3.bean.SuccessKilled;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

/**
 * @author : dk
 * @date : 2019/8/7 19:51
 * @description :
 */
@Mapper
public interface SuccessKilledMapper {

   /**
    * 插入一条秒杀记录
    * @param seckillId
    * @param userPhone
    * @return
    */
   int insertSuccessKilled(@Param("seckillId") Integer seckillId,@Param("userPhone") String userPhone,@Param("createTime") Date createTime);

   /**
    * 根据seckillId查询SuccessKilled对象，并携带Seckill对象
    * @param seckillId
    * @param userPhone
    * @return
    */
   SuccessKilled queryByIdWithSeckill(@Param("seckillId") Integer seckillId, @Param("userPhone") String userPhone);

}
