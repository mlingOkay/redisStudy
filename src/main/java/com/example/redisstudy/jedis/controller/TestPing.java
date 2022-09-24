package com.example.redisstudy.jedis.controller;
import redis.clients.jedis.Jedis;

public class TestPing {
	public static void main(String[] args) 
	{
		Jedis jedis = new Jedis("192.168.124.7",6379);
		System.out.println(jedis.ping());
	}
}
