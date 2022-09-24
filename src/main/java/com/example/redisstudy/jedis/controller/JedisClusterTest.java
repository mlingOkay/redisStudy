package com.example.redisstudy.jedis.controller;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import java.util.HashSet;
import java.util.Set;

/**
 * @Author: mlingOkay
 * @CreateTime: 2022-09-24  20:38
 * @Description: TODO
 * @Version: 1.0
 */
public class JedisClusterTest {
    public static void main(String[] args) {
        Set<HostAndPort> set =new HashSet<HostAndPort>();
        set.add(new HostAndPort("192.168.227.2",6379));
        JedisCluster jedisCluster=new JedisCluster(set);
        jedisCluster.set("k1", "v1");
        System.out.println(jedisCluster.get("k1"));
    }

}
