package com.example.demo3.mapper;

import com.example.demo3.bean.Seckill;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @author : dk
 * @date : 2019/8/7 19:51
 * @description :
 */
@Mapper
public interface SeckillMapper {
    /**
     *  根据id查看当前秒杀的商品
     * @param seckillId
     * @return
     */
    Seckill queryById(@Param("seckillId") Integer seckillId);

    /**
     * 根据所给参数查询 秒杀的商品
     * @param offset
     * @param limit
     * @return
     */
    List<Seckill> queryAll(@Param("offset")Integer offset , @Param("limit")Integer limit);


    /**
     * 返回所有商品列表
     * @return
     */
    List<Seckill> findAll();
    /**
     *  给当前商品的库存 -1
     * @param seckillId
     * @param killTime
     * @return
     */
    int reduceNumber(@Param("seckillId") long seckillId, @Param("killTime") Date killTime);
}
