package com.kgc.kmall.redisson.controller;


import com.kgc.kmall.util.RedisUtil;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import java.util.concurrent.locks.Lock;

/**
 * @author 张康硕
 * @create 2021-01-04 14:31
 */
@RestController
public class RedissonController {

    @Resource
    RedissonClient redissonClient;

    RedisUtil redisUtil=new RedisUtil();

    @RequestMapping("/test")
    public String testRedisson(){
        redisUtil.initPool("192.168.213.131",6379,0);
        Jedis jedis = redisUtil.getJedis();
        Lock lock = redissonClient.getLock("lock");// 声明锁
        lock.lock();//上锁
        try {
            String v = jedis.get("k");
            if (v==null) {
                v = "1";
            }
            System.out.println("->" + v);
            jedis.set("k", (Integer.parseInt(v) + 1) + "");
        }finally {
            jedis.close();
            lock.unlock();// 解锁
        }
        return "success";
    }

}
