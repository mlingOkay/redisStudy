package com.example.redisstudy.jedis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @Author: mlingOkay
 * @CreateTime: 2022-08-23  19:43
 * @Description: TODO
 * @Version: 1.0
 */
@RestController
@RequestMapping("/test")
public class redisCon {
    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping("one")
    public String testRedis() {
        //设置值到redis
        redisTemplate.opsForValue().setIfAbsent("name", "lucy");
        //从redis获取值
        String name = (String) redisTemplate.opsForValue().get("name");
        return name;
    }

    /**
     * [root@localhost ~]# ab -n 1000 -c 100 http://192.168.227.1:8080/test/testLock
     */
    @GetMapping("testLock")
    public void testLock(){
        //声明一个uuid,将作为一个value放入我们的key所对应的值中
        String uuid = UUID.randomUUID().toString();
        Boolean lock = redisTemplate.opsForValue().setIfAbsent("lock", uuid,3,TimeUnit.SECONDS);
        //2获取锁成功、查询num的值
        if (lock) {
            Object value = redisTemplate.opsForValue().get("num");
            //2.1判断num为空return
            if (StringUtils.isEmpty(value)) {
                return;
            }
            //不是孔 如果说在这出现了异常 那么delete就删除失败 也就是说锁永远存在
            //2.2有值就转成成int
            int num = Integer.parseInt(value + "");
            //2.3把redis的num加1
            redisTemplate.opsForValue().set("num",++num);
            if(uuid.equals((String)redisTemplate.opsForValue().get("lock"))){
                //2.4释放锁，del
                this.redisTemplate.delete("lock");
            }
        } else {
            //3获取锁失败、每隔0.1秒再获取
            try {
                Thread.sleep(100);
                testLock();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
