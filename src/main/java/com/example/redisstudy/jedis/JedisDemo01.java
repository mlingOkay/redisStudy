package com.example.redisstudy.jedis;

import redis.clients.jedis.Jedis;

/**
 * @Author: mlingOkay
 * @CreateTime: 2022-08-20  16:05
 * @Description: TODO
 * @Version: 1.0
 */
public class JedisDemo01 {
    public static void main(String[] args) {
        Jedis jedis = new Jedis("192.168.124.7", 6379);
        String pong = jedis.ping();
        System.out.println("连接成功"+pong);
    }
}
