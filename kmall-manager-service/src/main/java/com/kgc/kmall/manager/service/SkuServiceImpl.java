package com.kgc.kmall.manager.service;

import com.alibaba.fastjson.JSON;
import com.kgc.kmall.bean.*;
import com.kgc.kmall.manager.mapper.PmsSkuAttrValueMapper;
import com.kgc.kmall.manager.mapper.PmsSkuImageMapper;
import com.kgc.kmall.manager.mapper.PmsSkuInfoMapper;
import com.kgc.kmall.manager.mapper.PmsSkuSaleAttrValueMapper;
import com.kgc.kmall.manager.util.RedisUtil;
import com.kgc.kmall.service.SkuService;
import org.apache.dubbo.config.annotation.Service;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * @author 张康硕
 * @create 2020-12-24 9:23
 */
@Component
@Service
public class SkuServiceImpl  implements SkuService {
    @Resource
    PmsSkuInfoMapper pmsSkuInfoMapper;
    @Resource
    PmsSkuImageMapper pmsSkuImageMapper;
    @Resource
    PmsSkuAttrValueMapper pmsSkuAttrValueMapper;
    @Resource
    PmsSkuSaleAttrValueMapper pmsSkuSaleAttrValueMapper;
    @Resource
    RedisUtil redisUtil;
    @Resource
    RedissonClient redissonClient;

    @Override
    public String saveSkuInfo(PmsSkuInfo skuInfo) {
        pmsSkuInfoMapper.insert(skuInfo);
        Long skuInfoId=skuInfo.getId();
        for (PmsSkuImage pmsSkuImage : skuInfo.getSkuImageList()) {
            pmsSkuImage.setSkuId(skuInfoId);
            pmsSkuImageMapper.insert(pmsSkuImage);
        }
        for (PmsSkuAttrValue pmsSkuAttrValue : skuInfo.getSkuAttrValueList()) {

            pmsSkuAttrValue.setSkuId(skuInfoId);
            pmsSkuAttrValueMapper.insert(pmsSkuAttrValue);
        }
        for (PmsSkuSaleAttrValue pmsSkuSaleAttrValue :skuInfo.getSkuSaleAttrValueList()){

            pmsSkuSaleAttrValue.setSkuId(skuInfoId);
            pmsSkuSaleAttrValueMapper.insert(pmsSkuSaleAttrValue);
        }
        return "success";
    }



//    @Override
//    public PmsSkuInfo selectBySkuId(Long id) {
//        PmsSkuInfo pmsSkuInfo = pmsSkuInfoMapper.selectByPrimaryKey(id);
//        PmsSkuImageExample example = new PmsSkuImageExample();
//        example.createCriteria().andSkuIdEqualTo(pmsSkuInfo.getId());
//        List<PmsSkuImage> pmsSkuImages = pmsSkuImageMapper.selectByExample(example);
//        pmsSkuInfo.setSkuImageList(pmsSkuImages);
//
//        //缓存
//        Jedis jedis= redisUtil.getJedis();
//        String key="sku:"+id+":info";
//        String skuJson=jedis.get(key);
//        PmsSkuInfo SkuInfo=null;
//        if(skuJson!=null){
//            //缓存中有数据
//            SkuInfo = JSON.parseObject(skuJson, PmsSkuInfo.class);
//            jedis.close();
//            return SkuInfo;
//        }else {
//            //获取分布式锁
//            RLock lock=redissonClient.getLock(key);
//            lock.lock();//上锁
//            try {
//                //缓存中没有数据，从数据库中查询，并写入Redis
//                SkuInfo =pmsSkuInfoMapper.selectByPrimaryKey(id);
//                if (SkuInfo != null) {
//                    //保存到redis
//                    String json = JSON.toJSONString(SkuInfo);
//                    Random random = new Random();
//                    //有效期随机，防止缓存雪崩
//                    int i = random.nextInt(10);
//                    jedis.setex(key, i * 60 * 1000, json);
//                } else {
//                    jedis.setex(key, 5 * 60 * 1000, "empty");
//                }
//                jedis.close();
//            }finally {
//                lock.unlock();
//            }
//        }
//        return SkuInfo;
//    }

    @Override
    public PmsSkuInfo selectBySkuId(Long id) {
//        PmsSkuInfo pmsSkuInfo = pmsSkuInfoMapper.selectByPrimaryKey(id);
//        PmsSkuImageExample example = new PmsSkuImageExample();
//        example.createCriteria().andSkuIdEqualTo(pmsSkuInfo.getId());
//        List<PmsSkuImage> pmsSkuImages = pmsSkuImageMapper.selectByExample(example);
//        pmsSkuInfo.setSkuImageList(pmsSkuImages);
////        return pmsSkuInfo;
////
//        //缓存
        Jedis jedis= redisUtil.getJedis();
        String key="sku:"+id+":info";
        String skuJson=jedis.get(key);
        PmsSkuInfo SkuInfo=null;
        if(skuJson!=null){
            SkuInfo = JSON.parseObject(skuJson, PmsSkuInfo.class);
            jedis.close();
            return SkuInfo;
        }else {
            //使用nx分布式锁，避免缓存击穿
            String skuLockKey = "sku:" + id + ":lock";
            String skuLockValue= UUID.randomUUID().toString();
            String lock = jedis.set(skuLockKey, skuLockValue, "NX", "PX", 60 * 1000);
            //拿到分布式锁
            if (lock.equals("OK")) {
                SkuInfo = pmsSkuInfoMapper.selectByPrimaryKey(id);
                //防止缓存穿透，从DB中找不到数据也要缓存，但是缓存时间不要太长
                if (SkuInfo != null) {
                    //保存到redis
                    String json = JSON.toJSONString(SkuInfo);
                    Random random = new Random();
                    //有效期随机，防止缓存雪崩
                    int i = random.nextInt(10);
                    jedis.setex(key, i * 60 * 1000, json);
                } else {
                    jedis.setex(key, 5 * 60 * 1000, "empty");
                }
                //删除分布式锁，再删除之前获取value与之前的对比
//                String skuLockValue2 = jedis.get(skuLockKey);
//                if (skuLockValue2!=null&&skuLockValue2.equals(skuLockValue)) {
//                    jedis.del(skuLockKey);
//                }
                String script ="if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
                jedis.eval(script, Collections.singletonList(skuLockKey),Collections.singletonList(skuLockValue));
            } else {
                //如果分布式锁访问失败，线程休眠3秒，重新访问
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                selectBySkuId(id);
            }
        }
        return SkuInfo;
    }

    @Override
    public List<PmsSkuInfo> selectBySpuId(Long spuId) {
        return pmsSkuInfoMapper.selectBySpuId(spuId);
    }

    @Override
    public List<PmsSkuInfo> getAllSku() {
        List<PmsSkuInfo> pmsSkuInfos = pmsSkuInfoMapper.selectByExample(null);
        for (PmsSkuInfo pmsSkuInfo : pmsSkuInfos) {
            PmsSkuAttrValueExample example=new PmsSkuAttrValueExample();
            PmsSkuAttrValueExample.Criteria criteria = example.createCriteria();
            criteria.andSkuIdEqualTo(pmsSkuInfo.getId());
            List<PmsSkuAttrValue> pmsSkuAttrValues = pmsSkuAttrValueMapper.selectByExample(example);
            pmsSkuInfo.setSkuAttrValueList(pmsSkuAttrValues);
        }
        return pmsSkuInfos;
    }
}
