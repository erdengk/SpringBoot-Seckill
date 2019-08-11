package com.example.demo3.service.impl;

import com.example.demo3.bean.Seckill;
import com.example.demo3.bean.SuccessKilled;
import com.example.demo3.dto.Exposer;
import com.example.demo3.dto.SeckillExecution;
import com.example.demo3.enums.SeckillStatEnum;
import com.example.demo3.exception.RepeatKillException;
import com.example.demo3.exception.SeckillCloseException;
import com.example.demo3.exception.SeckillException;
import com.example.demo3.mapper.SeckillMapper;
import com.example.demo3.mapper.SuccessKilledMapper;
import com.example.demo3.redis.RedisDao;
import com.example.demo3.service.SeckillService;
import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.*;

import static com.example.demo3.enums.SeckillStatEnum.SUCCESS;

/**
 * @author : dk
 * @date : 2019/8/8 16:28
 * @description :
 */
@Service
public class SeckillServiceImpl implements SeckillService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    //设置盐值字符串，随便定义，用于混淆MD5值
    private final String salt = "sjajahjgnm00982jrfm;sd";

    //生成MD5值
    private String getMD5(Integer seckillId) {
        String base = seckillId + "/" + salt;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        //生成md5
        return md5;
    }


    @Autowired
    SeckillMapper seckillMapper;
    @Autowired
    SuccessKilledMapper successKilledMapper;

    @Autowired
    RedisDao redisDao;

    @Override
    public List<Seckill> findAll() {
        return seckillMapper.findAll();
    }

    @Override
    public Seckill getById(Integer seckillId) {
        return seckillMapper.queryById(seckillId);
    }

    @Override
    //优化暴露接口
    public Exposer exportSeckillUrl(Integer seckillId) {
        //优化点 ： 缓存优化  超时的基础上维护统一性
        // 1 访问 redis
        Seckill seckill = redisDao.getSeckill(seckillId);
        if(seckill == null)
        {
            //2 访问数据库
             seckill = seckillMapper.queryById(seckillId);
            if (seckill == null)
            {
                return  new Exposer(false,seckillId);
            }
            else
            {
//                3 放入 redis
                redisDao.putSeckill(seckill);
            }
        }
        Date startTieme = seckill.getStartTime();
        Date endTime = seckill.getEndTime();
        Date nowTime = new Date();
        if(nowTime.getTime() < startTieme.getTime()
                || nowTime.getTime() >endTime.getTime())
        //第一个判断条件是 秒杀未开始  第二个是秒杀已结束
        {
            return new Exposer(false,seckillId,nowTime.getTime(),startTieme.getTime(),endTime.getTime());
        }
        String md5 = getMD5(seckillId);
        return  new Exposer(true,md5,seckillId);
    }
    /**
     * 使用注解式事务方法的有优点：开发团队达成了一致约定，明确标注事务方法的编程风格
     * 使用事务控制需要注意：
     * 1.保证事务方法的执行时间尽可能短，不要穿插其他网络操作PRC/HTTP请求（可以将这些请求剥离出来）
     * 2.不是所有的方法都需要事务控制，如只有一条修改的操作、只读操作等是不需要进行事务控制的
     *
     * Spring默认只对运行期异常进行事务的回滚操作，对于编译异常Spring是不进行回滚的，所以对于需要进行事务控制的方法尽可能将可能抛出的异常都转换成运行期异常
     */
    @Override
    @Transactional
    public SeckillExecution executeSeckill(Integer seckillId, String userPhone, String md5) throws SeckillException, RepeatKillException, SeckillCloseException {
        if (md5 == null || !md5.equals(getMD5(seckillId))) {
            throw new SeckillException("seckill data rewrite");
        }
        //执行秒杀逻辑：1.减库存；2.储存秒杀订单
        Date nowTime = new Date();
        try {
            int insertCount = successKilledMapper.insertSuccessKilled(seckillId, userPhone);
            if (insertCount <= 0) {
                //重复秒杀
//                    throw new RepeatKillException("seckill repeated");
                return new SeckillExecution(seckillId, SeckillStatEnum.REPEAT_KILL,"重复秒杀");
            } else {
                // 减库存、热点商品的竞争
                int updateCount = seckillMapper.reduceNumber(seckillId, nowTime);
                if (updateCount <= 0) {
                    //没有更新记录，秒杀结束，rollback
                    throw new SeckillCloseException("seckill is closed");
                } else {
                    //秒杀成功 commit
                    SuccessKilled successKilled = successKilledMapper.queryByIdWithSeckill(seckillId, userPhone);
                    return new SeckillExecution(seckillId, SUCCESS,"秒杀成功",successKilled);
                }
            }
        } catch (SeckillCloseException e) {
            throw e;
        } catch (RepeatKillException e) {
            throw e;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            //所有编译期异常，转换为运行期异常
            throw new SeckillException("seckill inner error:" + e.getMessage());
        }
    }

    @Override
    public SeckillExecution executeSeckillProducedure(Integer seckillId, String userPhone, String md5) {
        if (md5 == null || !md5.equals(getMD5(seckillId)))
        {
            return  new SeckillExecution(seckillId,SeckillStatEnum.DATA_REWRITE,"数据串改");
        }
//        Date killTime = new Date();
        Date killTime = Calendar.getInstance().getTime();
        Map<String,Object> map = new HashMap<>();
        map.put("seckillId",seckillId);
        map.put(("phone"),userPhone);
        map.put("killTime",killTime);
        map.put("result",null);
        // 执行存储过程，result 被赋值
        try {
            seckillMapper.killByProcedure(map);
            int result = MapUtils.getInteger(map,"result",-2);
            if (result == 1)
            {
                SuccessKilled successKilled = successKilledMapper.queryByIdWithSeckill(seckillId,userPhone);
                return  new SeckillExecution(seckillId, SUCCESS,"success",successKilled);
            }
            else
            {
                return  new SeckillExecution(seckillId,SeckillStatEnum.stateOf(result),SeckillStatEnum.stateOf(result).getStateInfo());
            }
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return  new SeckillExecution(seckillId,SeckillStatEnum.INNER_ERROR,"系统错误");
        }
    }
}
