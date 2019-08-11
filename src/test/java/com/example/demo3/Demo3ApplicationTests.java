package com.example.demo3;

import com.example.demo3.bean.Seckill;
import com.example.demo3.bean.SuccessKilled;
import com.example.demo3.dto.Exposer;
import com.example.demo3.dto.SeckillExecution;
import com.example.demo3.exception.RepeatKillException;
import com.example.demo3.exception.SeckillCloseException;
import com.example.demo3.mapper.SeckillMapper;
import com.example.demo3.mapper.SuccessKilledMapper;
import com.example.demo3.redis.RedisDao;
import com.example.demo3.service.SeckillService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Demo3ApplicationTests {


    @Autowired
    SeckillMapper seckillMapper;

    @Test
    public void reduceNumber() {
        Date date = new Date();
        int up = seckillMapper.reduceNumber(1,date);
        System.out.println(up);
    }

    @Test
    public void queryById() {
        seckillMapper.queryById(1);
        System.out.println(1);
    }
    @Test
    public void queryAll() {
        List<Seckill > seckills = seckillMapper.queryAll(1,10);
        for (int i = 0; i <seckills.size() ; i++) {
            System.out.println(seckills.get(i).toString());
        }
    }

    @Autowired
    SuccessKilledMapper successkilledMapper;

    @Test
    public void insertSuccessKilled() {
        int  s = successkilledMapper.insertSuccessKilled(1,"sssss");
        System.out.println(s);

    }

    @Test
    public void queryByIdWithSeckill() {
        SuccessKilled successkilled = successkilledMapper.queryByIdWithSeckill(1,"sssss");
        System.out.println(successkilled);
        System.out.println(successkilled.getSeckill());
    }
    /**
     * SuccessKilled(seckillId=1, userPhone=sssss, state=0, createTime=null,
     * seckill=Seckill(seckillId=1, name=100秒杀ipad, number=null, startTime=Thu Aug 22 00:27:13 CST 2019, endTime=Fri Aug 23 00:27:16 CST 2019, createTime=Fri Aug 09 00:27:20 CST 2019))
     * Seckill
     * (seckillId=1, name=100秒杀ipad, number=null, startTime=Thu Aug 22 00:27:13 CST 2019, endTime=Fri Aug 23 00:27:16 CST 2019, createTime=Fri Aug 09 00:27:20 CST 2019)
     */


    @Autowired
    SeckillService seckillService;

//    @Test
//    public void getSeckillList() {
//        List <Seckill> seckills = seckillService.getSeckillList();
//        for (int i = 0; i < seckills.size(); i++) {
//            System.out.println(seckills.get(i).toString());
//        }
//    }

    @Test
    public void getById() {
        Seckill seckill = seckillService.getById(1);
        System.out.println(seckill.toString());
    }

    @Test
    public void exportSeckillUrl() {
        Exposer exposer = seckillService.exportSeckillUrl(1);
        //测试时 要注意数据库中的时间
//        INSERT INTO `spring`.`seckill`
//        (`seckill_id`, `name`, `number`, `start_time`, `end_time`, `create_time`)
//        VALUES
//        ('1', '100秒杀ipad', '100', '2019-08-07 16:27:13', '2019-08-10 16:27:16', '2019-08-06 16:27:20');

        System.out.println(exposer.toString());
//        Exposer{exposed=true, md5='05fd17ce7b3fb01e5c9fb08e4f7004c8', seckillId=1, now=0, start=0, end=0}
    }

    @Test
    public void executeSeckill() {
        String md5 = "05fd17ce7b3222fb01e5c9fb08e4f7004c8";
        SeckillExecution seckillExecution = seckillService.executeSeckill(1,"15256466666",md5);
        System.out.println(seckillExecution.toString());
//再次使用相同的手机号去秒杀的时候会报错   com.example.demo3.exception.RepeatKillException: seckill repeated
    }



    @Test
    public void testSeckillLogic() throws Exception {
        Exposer exposer = seckillService.exportSeckillUrl(1);
        if (exposer.isExposed()) {
            Integer id = exposer.getSeckillId();
            String md5 = exposer.getMd5();
            try {
                SeckillExecution seckillExecution = seckillService.executeSeckill(2,"1221111222", md5);
                System.out.println("秒杀开启");
            } catch (SeckillCloseException e) {
                System.out.println(e.getMessage());
            } catch (RepeatKillException e1) {
                System.out.println(e1.getMessage());
            }
        } else {
            //秒杀未开启
            System.out.println("秒杀未开启");
        }
    }

    @Autowired
    RedisDao redisDao;

    private Integer id =1;

    @Test
    public void Seckill() {
//        get and put
        Seckill seckill = redisDao.getSeckill(id);
        if (seckill == null)
        {
            seckill = seckillMapper.queryById(id);
            if(seckill != null)
            {
                String result = redisDao.putSeckill(seckill);
                System.out.println(result);
                seckill = redisDao.getSeckill(id);
                System.out.println(seckill);
            }
        }
    }

    @Test
    public void executeSeckillProducedure() {
        int seckillId = 1;
        String phone = "15596520256" ;
        Exposer exposer = seckillService.exportSeckillUrl(seckillId);
        if (exposer.isExposed())
        {
            String md5 = exposer.getMd5();
            SeckillExecution seckillExecution = seckillService.executeSeckillProducedure(seckillId, phone, md5);
            System.out.println(seckillExecution.getStateInfo());
        }
    }
}
