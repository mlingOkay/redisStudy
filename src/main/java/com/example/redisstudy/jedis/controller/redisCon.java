package com.example.redisstudy.jedis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @GetMapping("testLock")
    public void testLock(){
        //1获取锁，setne 类似于 setnx lock 111
        Boolean lock = redisTemplate.opsForValue().setIfAbsent("lock", "111");
        //2获取锁成功、查询num的值
        if (lock) {
            Object value = redisTemplate.opsForValue().get("num");
            //2.1判断num为空return
            if (StringUtils.isEmpty(value)) {
                return;
            }
            //2.2有值就转成成int
            int num = Integer.parseInt(value + "");
            //2.3把redis的num加1
            redisTemplate.opsForValue().set("num", ++num);
            //2.4释放锁，del
            redisTemplate.delete("lock");

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
